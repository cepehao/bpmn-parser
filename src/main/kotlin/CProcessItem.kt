// класс-родитель элементов процесса
open class CProcessItem(
    val name: String,
    private val incomingIdList: ArrayList<String>, // по строковым идентификаторам связей будем строить связи для
    private val outgoingIdList: ArrayList<String> // объектов (в бд строковые идентификаторы связей хранить не будем)
)
{

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