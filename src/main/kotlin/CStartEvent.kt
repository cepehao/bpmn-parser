//начальное действие
class CStartEvent(_name:String, _id:String, _outgoingId:String) {

    private val name:String = _name
    private val id:String = _id
    private val outgoingId:String = _outgoingId //следующее действие

}