//начальное действие
class CStartEvent(_name:String, _id:String, _outgoingId:String) {

    private val name:String = _name
    private val id:String = _id
    private val outgoingId:String = _outgoingId //следующее действие

    fun getId():String {
        return id
    }

    fun getName():String {
        return name
    }

    fun getOutgoingId():String{
        return outgoingId
    }
}