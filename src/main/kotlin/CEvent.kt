class CEvent (
    _name: String, _incomingList: ArrayList<CIncoming>,
    _outgoingList: ArrayList<COutgoing>, _eventType: EEventType
    )
{
    val name:String = _name
    val incomingList: ArrayList<CIncoming> = _incomingList
    val outgoingList: ArrayList<COutgoing> = _outgoingList
    val eventType: EEventType = _eventType
}