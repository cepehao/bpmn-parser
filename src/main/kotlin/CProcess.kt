import java.util.UUID

// весь процесс
class CProcess(
    name: String,
    id: UUID
)
{

    var tasks = mutableMapOf<String, CTask>()
    var events = mutableMapOf<String, CEvent>()
    var gateways = mutableMapOf<String, CGateway>()

    fun addTask(id: String, task: CTask) {
        tasks[id] = task
    }

    fun addGateway(id: String, gateway: CGateway) {
        gateways[id] = gateway
    }

    fun addEvent(id: String, event: CEvent) {
        events[id] = event
    }

}