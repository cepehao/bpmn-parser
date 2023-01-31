//конечное действие
class CEndEvent(_name:String, _incomingId:String) {

    private val name:String = _name
    //private val id:String = _id
    private val incomingId:String = _incomingId

//    fun getId():String {
//        return id
//    }

    fun getName():String {
        return name
    }

    fun getIncomingId():String{
        return incomingId
    }
}