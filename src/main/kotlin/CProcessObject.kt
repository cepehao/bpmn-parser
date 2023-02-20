// класс-родитель объектов процесса
open class CProcessObject(_name: String)
{

    val name = _name

    var incomingList = arrayListOf<CProcessObject?>()
    var outgoingList = arrayListOf<CProcessObject?>()

    fun addConnectionList(incomingList: ArrayList<CProcessObject?>, outgoingList: ArrayList<CProcessObject?>) {
        this.incomingList = incomingList
        this.outgoingList = outgoingList
    }

}