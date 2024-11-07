function RoomsPanel() {

    const rooms = [...Array(100).keys()]
    const comp = rooms.map((room) => <li key={room}>{"Room " + room}</li>)

    return (
        <ul id="rooms-panel">
            {comp}
        </ul>
    )
}

export default RoomsPanel;