import "./RoomPage.css"
import RoomsPanel from "./RoomsPanel.jsx";
import Header from "./Header.jsx";

function RoomPage() {
    return (
        <div className={"room-page"}>
            <Header/>
            <RoomsPanel/>
            <div id={"main"}>
                <div>
                    Room 1
                </div>
                <div id={"messages"}>

                </div>
                <div>
                    <input id={"message-field"} type={"text"}/>
                    <button>Send</button>
                </div>
            </div>
            <div id={"members-panel"}></div>
        </div>
    )
}

export default RoomPage;