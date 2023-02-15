class CGateway (
    _name: String, _incomingList: ArrayList<CIncoming>,
    _outgoingList: ArrayList<COutgoing>, _gatewayType: EGateway
)
{
    val name:String = _name
    val incomingList: ArrayList<CIncoming> = _incomingList
    val outgoingList: ArrayList<COutgoing> = _outgoingList
    val gatewayType: EGateway = _gatewayType
}