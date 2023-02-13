import java.util.UUID

//весь процесс
class CProcess(_name: String, _id: UUID) {

    val name: String = _name
    val id: UUID = _id //можно не запоминать id процесса из bpmn т.к.
                                //для каждого нового процесса будет приходить uuid из веба?

    var tasks = mutableMapOf<String, CTask>()
    var intermediateCatchEvents = mutableMapOf<String, CIntermediateCatchEvent>()
    var endEvents = mutableMapOf<String, CEndEvent>()
    var exclusiveGateways = mutableMapOf<String, CExclusiveGateway>()

    var sstartEvent: CStartEvent? = null

    fun setStartEvent(_sstartEvent: CStartEvent) {
        sstartEvent = _sstartEvent
    }

    fun addTask(_id:String, _task: CTask) {
        tasks[_id] = _task
    }

    fun addIntermediateCatchEvent(_id:String, _intermediateCatchEvent: CIntermediateCatchEvent) {
        intermediateCatchEvents[_id] = _intermediateCatchEvent
    }

    fun addEndEvent(_id:String, _endEvent:CEndEvent) {
        endEvents[_id] = _endEvent
    }

    fun addExclusiveGateway(_id:String, _exclusiveGateway: CExclusiveGateway) {
        exclusiveGateways[_id] = _exclusiveGateway
    }
}