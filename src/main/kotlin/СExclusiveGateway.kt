//блок выбора
class CExclusiveGateway(_name:String, _incomingId:String, _outgoingIdList:ArrayList<String>) {

    private val name:String = _name
    private val incomingId = _incomingId
    private val outgoingIdList = _outgoingIdList //список возможных последующих действий

    fun getName():String {
        return name
    }

    fun getIncomingId():String{
        return incomingId
    }

}