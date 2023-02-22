// класс-родитель объектов процесса
open class CProcessItem(
    _name: String,
    _incomingIdList: ArrayList<String>,
    _outgoingIdList: ArrayList<String>
)
{

    val name = _name
    private val incomingIdList = _incomingIdList
    private val outgoingIdList = _outgoingIdList

    var incomingList = arrayListOf<CProcessItem?>()
    var outgoingList = arrayListOf<CProcessItem?>()

    fun addConnectionList(incomingList: ArrayList<CProcessItem?>, outgoingList: ArrayList<CProcessItem?>) {
        this.incomingList = incomingList
        this.outgoingList = outgoingList
    }

    fun checkIncoming(id: String): Boolean {
        return id in incomingIdList
    }

    fun checkOutgoing(id: String): Boolean {
        return id in outgoingIdList
    }

    fun getIncomingIdList(): ArrayList<String> {
        return incomingIdList
    }

    fun getOutgoingIdList(): ArrayList<String> {
        return outgoingIdList
    }
}