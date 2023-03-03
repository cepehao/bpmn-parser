// логическое правило
class CGateway (
    name: String,
    incomingIdList: ArrayList<String>,
    outgoingIdList: ArrayList<String>,
    val gatewayType: EGateway
) : CProcessItem(name, incomingIdList, outgoingIdList)
{


}