//весь процесс
class CProcess(_name:String, _id:String) {

    private val name:String = _name
    private val id:String = _id //можно не запоминать id процесса из bpmn т.к.
                                //для каждого нового процесса будет приходить uuid из веба?


    var tasks = mutableMapOf<String, CTask>()
    var intermediateCatchEvents = mutableMapOf<String, CIntermediateCatchEvent>()
    var endEvents = mutableMapOf<String, CEndEvent>()
    var exclusiveGateways = mutableMapOf<String, CExclusiveGateway>()

    private lateinit var startEvent:CStartEvent
    //private lateinit var task:CTask
    //private lateinit var endEvent:CEndEvent

    fun getId():String {
        return id
    }

    fun getName():String {
        return name
    }

    fun setStartEvent(_startEvent: CStartEvent) {
        startEvent = _startEvent
    }

    fun addTask(_id:String, _task:CTask) {
        tasks[_id] = _task
    }

    fun addIntermediateCatchEvent(_id:String, _intermediateCatchEvent:CIntermediateCatchEvent) {
        intermediateCatchEvents[_id] = _intermediateCatchEvent
    }

    fun addEndEvent(_id:String, _endEvent:CEndEvent) {
        endEvents[_id] = _endEvent
    }

    fun addExclusiveGateway(_id:String, _exclusiveGateway:CExclusiveGateway) {
        exclusiveGateways[_id] = _exclusiveGateway
    }
}