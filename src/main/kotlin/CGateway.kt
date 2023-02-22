// логическое правило
class CGateway (
    _name: String,
    _gatewayType: EGateway,
    _incomingIdList: ArrayList<String>,
    _outgoingIdList: ArrayList<String>
) : CProcessItem(_name, _incomingIdList, _outgoingIdList)
{

    val gatewayType = _gatewayType

}