// событие
class CEvent  (
    _name: String,
    _eventType: EEventType,
    _incomingIdList: ArrayList<String>,
    _outgoingIdList: ArrayList<String>
) : CProcessItem(_name, _incomingIdList, _outgoingIdList)
{

    val eventType = _eventType

}