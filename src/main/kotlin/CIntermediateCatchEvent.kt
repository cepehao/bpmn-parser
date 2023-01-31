//промежуточное действие
class CIntermediateCatchEvent(_name:String, _incomingId:String, _outgoingId:String) {

    private val name:String = _name
    private val incomingId = _incomingId
    private val outgoingId = _outgoingId

    fun getName():String {
        return name
    }

    fun getIncomingId():String{
        return incomingId
    }

    fun getOutgoingId():String{
        return outgoingId
    }
}