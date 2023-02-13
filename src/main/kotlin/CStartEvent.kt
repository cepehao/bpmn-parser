//начальное действие
class CStartEvent(_name:String, _id:String, _outgoingId:String) {

    val name:String = _name
    val id:String = _id
    val outgoingId:String = _outgoingId //следующее действие

}