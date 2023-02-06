//промежуточное действие
class CIntermediateCatchEvent(_name:String, _incomingId:String, _outgoingId:String) {

    private val name:String = _name
    private val incomingId = _incomingId //предыдущее действие
    private val outgoingId = _outgoingId //следующее действие

}