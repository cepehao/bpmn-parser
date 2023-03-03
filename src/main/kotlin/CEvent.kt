// событие
class CEvent  (
    name: String,
    incomingIdList: ArrayList<String>,
    outgoingIdList: ArrayList<String>,
    val eventType: EEventType
) : CProcessItem(name, incomingIdList, outgoingIdList)
{

}