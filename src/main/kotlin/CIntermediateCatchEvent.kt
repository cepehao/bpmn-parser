//промежуточное действие
class CIntermediateCatchEvent(_name:String, _incomingId:String, _outgoingId:String) {

    val name:String = _name
    val incomingId = _incomingId //предыдущее действие
    val outgoingId = _outgoingId //следующее действие

}