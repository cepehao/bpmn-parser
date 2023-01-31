//конечное действие
class CEndEvent(_name:String, _incomingId:String) {

    private val name:String = _name
    private val incomingId:String = _incomingId //предыдущее действие

    fun getName():String {
        return name
    }

    fun getIncomingId():String{
        return incomingId
    }
}