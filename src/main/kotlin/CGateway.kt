// логическое правило
class CGateway (
    name: String,
    incomingIdList: ArrayList<String>,
    outgoingIdList: ArrayList<String>,
    val gatewayType: EGatewayType
) : CProcessItem(name, incomingIdList, outgoingIdList)
{

}