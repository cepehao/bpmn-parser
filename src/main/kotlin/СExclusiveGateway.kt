//блок выбора
class CExclusiveGateway(_name:String, _incomingId:String, _outgoingIdList:ArrayList<String>) {

    val name:String = _name
    val incomingId = _incomingId
    val outgoingIdList = _outgoingIdList //список возможных последующих действий

}