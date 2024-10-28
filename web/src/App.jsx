import {useState} from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import Header from './components/header.jsx'
import {Tospi} from '../../shared/network/build/dist/js/developmentLibrary/GatherSpace-shared-network.mjs';
// import {jsSendMessage} from '../../shared/network/build/dist/js/developmentLibrary/GatherSpace-shared-network.mjs';

const test = new Tospi()

test.jsConnect()

function App() {


    const [count, setCount] = useState(0)
    const [message, setMessage] = useState("")

    return (
        <>
            <Header/>
            <div>
                <a href="https://vitejs.dev" target="_blank">
                    <img src={viteLogo} className="logo" alt="Vite logo"/>
                </a>
                <a href="https://react.dev" target="_blank">
                    <img src={reactLogo} className="logo react" alt="React logo"/>
                </a>
            </div>
            <h1>Vite + React</h1>
            <div className="card">
                <button onClick={() => setCount((count) => count + 1)}>
                    count is {count}
                </button>
                <p>
                    Edit <code>src/App.jsx</code> and save to test HMR
                </p>
            </div>
            <input type="text" value={message} onChange={(msg) => setMessage(msg.target.value)}/>
            <button onClick={() => {test.jsSendMessage(message)}}>Submit</button>
            <p className="read-the-docs">
                Click on the Vite and React logos to learn more
            </p>
        </>
    )
}

export default App
