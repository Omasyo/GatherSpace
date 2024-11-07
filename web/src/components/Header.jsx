import './Header.css'

function Header() {
    return (
        <header>
            <h1>GatherSpace</h1>
            <div style={{flexGrow: 1}}>
                <input id={"searchbar"} type={"text"} placeholder={"Search for a Room"}/>
            </div>
            <div>
                <button>Login</button>
                <button>Signup</button>
            </div>
        </header>
    )
}

export default Header