// класс-родитель элементов процесса
open class CProcessItem(
    _name: String,
    _incomingIdList: ArrayList<String>,
    _outgoingIdList: ArrayList<String>
)
{

    val name = _name
    private val incomingIdList = _incomingIdList // по строковым идентификаторам связей будем строить связи для
    private val outgoingIdList = _outgoingIdList // объектов (в бд строковые идентификаторы связей хранить не будем)

    var incomingItemList = arrayListOf<CProcessItem?>() // тут будут храниться ссылки на связывающие
    var outgoingItemList = arrayListOf<CProcessItem?>() // элементы процесса

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