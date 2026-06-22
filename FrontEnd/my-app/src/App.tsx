import { useState } from 'react'
import './App.css'

const API = 'http://localhost:8080/jogo'

type CartaType = {
  simbolo: string
  naipe: string
  valor: number
}


function Header() {
  return (
    <header className="header">
      <h1>♠ Jogo 21 ♠</h1>
    </header>
  )
}


type CartaProps = {
  simbolo: string
  naipe: string
  virada?: boolean
}


function converterNaipe(naipe: string): string {
  const naipes: Record<string, string> = {
    'Espadas': '♠',
    'Copas':   '♥',
    'Ouros':   '♦',
    'Paus':    '♣',
  }
  return naipes[naipe] ?? naipe
}

function Carta({ simbolo, naipe, virada = false }: CartaProps) {
  if (virada) {
    return <div className="carta carta-virada" />
  }

  const vermelha = naipe === 'Copas' || naipe === 'Ouros'

  return (
    <div className="carta" style={{ color: vermelha ? 'red' : 'black' }}>
      <p>{simbolo}</p>
      <p>{converterNaipe(naipe)}</p>
    </div>
  )
}


type MesaProps = {
  maoDealer: CartaType[]
  maoJogador: CartaType[]
  jogoTerminado: boolean
}

function Mesa({ maoDealer, maoJogador, jogoTerminado }: MesaProps) {
  return (
    <section className="mesa">
      <div>
        <h2>Dealer</h2>
        <div className="cartas">
          {maoDealer.map((carta, index) => (
            <Carta
              key={index}
              simbolo={carta.simbolo}
              naipe={carta.naipe}
              virada={index === 1 && !jogoTerminado}
            />
          ))}
        </div>
      </div>

      <hr />

      <div>
        <h2>Jogador</h2>
        <div className="cartas">
          {maoJogador.map((carta, index) => (
            <Carta key={index} simbolo={carta.simbolo} naipe={carta.naipe} />
          ))}
        </div>
      </div>
    </section>
  )
}


type PlacarProps = {
  pontuacaoDealer: number
  pontuacaoJogador: number
  jogoTerminado: boolean
}

function Placar({ pontuacaoDealer, pontuacaoJogador, jogoTerminado }: PlacarProps) {
  return (
    <div className="placar">
      <p>Dealer: <strong>{jogoTerminado ? pontuacaoDealer : '?'}</strong></p>
      <p>Jogador: <strong>{pontuacaoJogador}</strong></p>
    </div>
  )
}


type ControlesProps = {
  onPedirCarta: () => void
  onParar: () => void
  jogoTerminado: boolean
}

function Controles({ onPedirCarta, onParar, jogoTerminado }: ControlesProps) {
  return (
    <div className="controles">
      <button onClick={onPedirCarta} disabled={jogoTerminado}>Pedir Carta</button>
      <button onClick={onParar} disabled={jogoTerminado}>Parar</button>
    </div>
  )
}


type PopupProps = {
  resultado: string
  partidaId: number | null
  onNovaRodada: () => void
}

function PopupResultado({ resultado, partidaId, onNovaRodada }: PopupProps) {
  const mensagem =
    resultado === 'JOGADOR' ? 'Vitória!' :
    resultado === 'DEALER'  ? 'Derrota!'  :
    resultado === 'EMPATE'  ? 'Empate!'   :
    resultado

  return (
    <div className="popup-overlay">
      <div className="popup">
        <h2>{mensagem}</h2>
        {partidaId && <p style={{ fontSize: '0.9rem', opacity: 0.7 }}>Partida #{partidaId} salva</p>}
        <button onClick={onNovaRodada}>Nova Rodada</button>
      </div>
    </div>
  )
}


function App() {
  const [maoDealer, setMaoDealer] = useState<CartaType[]>([])
  const [maoJogador, setMaoJogador] = useState<CartaType[]>([])

  const [pontuacaoJogador, setPontuacaoJogador] = useState(0)
  const [pontuacaoDealer, setPontuacaoDealer] = useState(0)

  const [jogoTerminado, setJogoTerminado] = useState(false)
  const [resultado, setResultado] = useState('')

  const [partidaId, setPartidaId] = useState<number | null>(null)

  const [nomeJogador, setNomeJogador] = useState('')
  const [jogoIniciado, setJogoIniciado] = useState(false)

  async function novaRodada() {
    const resposta = await fetch(`${API}/iniciar?nomeJogador=${encodeURIComponent(nomeJogador)}`, {
      method: 'POST'
    })
    const dados = await resposta.json()

    setMaoJogador(dados.maoJogador)
    setMaoDealer(dados.maoDealer)
    setPontuacaoJogador(dados.pontuacaoJogador)
    setPontuacaoDealer(0)
    setJogoTerminado(false)
    setResultado('')
    setPartidaId(null)
    setJogoIniciado(true)
  }

  async function pedirCarta() {
    const resposta = await fetch(`${API}/pedir`, { method: 'POST' })
    const dados = await resposta.json()

    setMaoJogador(dados.maoJogador)
    setPontuacaoJogador(dados.pontuacaoJogador)

    if (dados.status === 'DERROTA') {
      setJogoTerminado(true)
      setResultado('DEALER')
    }
  }

  async function parar() {
    const resposta = await fetch(`${API}/parar`, { method: 'POST' })
    const dados = await resposta.json()

    setMaoDealer(dados.maoDealer)
    setPontuacaoJogador(dados.pontuacaoJogador)
    setPontuacaoDealer(dados.pontuacaoDealer)
    setJogoTerminado(true)
    setResultado(dados.resultado)
  }

  return (
    <div className="app">
      <Header />
      <main>
        {}
        {!jogoIniciado ? (
          <div className="formulario">
            <h2>Bem-vindo ao Jogo 21!</h2>
            <input
              type="text"
              placeholder="Digite seu nome"
              value={nomeJogador}
              onChange={(e) => setNomeJogador(e.target.value)}
              onKeyDown={(e) => e.key === 'Enter' && nomeJogador.trim() && novaRodada()}
            />
            <button onClick={novaRodada} disabled={!nomeJogador.trim()}>
              Iniciar Jogo
            </button>
          </div>
        ) : (
          <>
            <Placar
              pontuacaoDealer={pontuacaoDealer}
              pontuacaoJogador={pontuacaoJogador}
              jogoTerminado={jogoTerminado}
            />
            <Mesa
              maoDealer={jogoTerminado ? maoDealer : [...maoDealer, { simbolo: "?", naipe: "?", valor: 0 }]}
              maoJogador={maoJogador}
              jogoTerminado={jogoTerminado}
            />
            <Controles
              onPedirCarta={pedirCarta}
              onParar={parar}
              jogoTerminado={jogoTerminado}
            />
            {jogoTerminado && resultado && (
              <PopupResultado
                resultado={resultado}
                partidaId={partidaId}
                onNovaRodada={novaRodada}
              />
            )}
          </>
        )}
      </main>
    </div>
  )
}

export default App
