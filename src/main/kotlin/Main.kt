import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory

// собираем элемент процесса - событие, связи пока храним строковыми идентификаторами
fun createEvent(curNode: Node, eventType: EEventType): CEvent {
    val nodeList = curNode.childNodes
    var node: Node

    val incomingIdList = arrayListOf<String>()
    val outgoingIdList = arrayListOf<String>()

    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node.nodeType != Node.ELEMENT_NODE) continue

        if (node.nodeName == "semantic:incoming") {
            incomingIdList.add(node.textContent.substring(3))
        }

        if (node.nodeName == "semantic:outgoing") {
            outgoingIdList.add(node.textContent.substring(3))
        }
    }

    return CEvent(curNode.attributes.getNamedItem("name").nodeValue, incomingIdList, outgoingIdList, eventType)
}

fun createGateway(curNode: Node, gatewayType: EGatewayType): CGateway { // todo правило выбора
    val nodeList = curNode.childNodes
    var node: Node

    val incomingIdList = arrayListOf<String>()
    val outgoingIdList = arrayListOf<String>()

    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node.nodeType != Node.ELEMENT_NODE) continue

        if (node.nodeName == "semantic:incoming") {
            incomingIdList.add(node.textContent.substring(3))
        }

        if (node.nodeName == "semantic:outgoing") {
            outgoingIdList.add(node.textContent.substring(3))
        }
    }

    return CGateway(curNode.attributes.getNamedItem("name").nodeValue, incomingIdList, outgoingIdList, gatewayType)
}

fun createTask(curNode: Node): CTask {
    val nodeList = curNode.childNodes
    var node: Node

    val incomingIdList = arrayListOf<String>()
    val outgoingIdList = arrayListOf<String>()


    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node.nodeType != Node.ELEMENT_NODE) continue

        if (node.nodeName == "semantic:incoming") {
            incomingIdList.add(node.textContent.substring(3))
        }

        if (node.nodeName == "semantic:outgoing") {
            outgoingIdList.add(node.textContent.substring(3))
        }
    }

    return CTask(curNode.attributes.getNamedItem("name").nodeValue, incomingIdList, outgoingIdList)
}

// создаем карту всех процессов, вместе со строковыми идентификаторами связей, без ссылок на другие элементы процесса
fun getProcessObjectsMap(nodeList: NodeList): MutableMap<String, CProcessItem> {
    var node: Node
    val processObjectsMap = mutableMapOf<String, CProcessItem>()

    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node.nodeType != Node.ELEMENT_NODE) continue

        when(node.nodeName) {
            "semantic:startEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                                createEvent(node, EEventType.START))
            }

            "semantic:task" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                                createTask(node))
            }

            "semantic:endEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    createEvent(node, EEventType.END))
            }

            "semantic:intermediateCatchEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    createEvent(node, EEventType.INTERMEDIATE))
            }

            "semantic:exclusiveGateway" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    createGateway(node, EGatewayType.EXCLUSIVE))
            }
        }
    }

    return processObjectsMap
}

//проверить список строковых идентификаторов на входы/выходы в другие элементы процесса, вернуть список элементов процесса
fun createIncomingItemList (items: MutableMap<String, CProcessItem>, incomingIdList: ArrayList<String>): ArrayList<CProcessItem?> {
    val incomingList = arrayListOf<CProcessItem?>()

    for (id in incomingIdList) {
        for (item in items.values) {
            if (item.checkOutgoing(id)) incomingList.add(item)
        }
    }

    return incomingList
}

fun createOutgoingItemList (items: MutableMap<String, CProcessItem>, outgoingIdList: ArrayList<String>): ArrayList<CProcessItem?> {
    val outgoingList = arrayListOf<CProcessItem?>()

    for (id in outgoingIdList) {
        for (item in items.values) {
            if (item.checkIncoming(id)) outgoingList.add(item)
        }
    }

    return outgoingList
}

// добавляем в карту элементов процесса связи ссылками на объекты
fun addConnections(items: MutableMap<String, CProcessItem>) {

    var incomingIdList: ArrayList<String>
    var outgoingIdList: ArrayList<String>
    var incomingItemList: ArrayList<CProcessItem?>
    var outgoingItemList: ArrayList<CProcessItem?>

    for (item in items.values) {
        incomingIdList = item.getIncomingIdList() // получили список строковых идентификаторов
        outgoingIdList = item.getOutgoingIdList() // связей из i-ого объекта

        incomingItemList = createIncomingItemList(items, incomingIdList) // теперь определим на какие именно
        outgoingItemList = createOutgoingItemList(items, outgoingIdList) // элементы процесса связывают эти идентификаторы

        item.incomingItemList = incomingItemList
        item.outgoingItemList = outgoingItemList
    }

}

// собираем из карты элементов процесса итоговый процесс целиком
fun makeProcess(process: CProcess, processItemMap: MutableMap<String, CProcessItem>) {
    for (entry in processItemMap.entries.iterator()) {
        when (entry.value) {
            is CEvent -> process.addEvent(entry.key, entry.value as CEvent)

            is CTask -> process.addTask(entry.key, entry.value as CTask)

            is CGateway -> process.addGateway(entry.key, entry.value as CGateway)
        }
    }
}

fun parseBPMN(uuid: UUID, file: File): CProcess? { // todo MultipartFile -> File
    var process: CProcess? = null

    try {
        val dbf = DocumentBuilderFactory.newInstance()
        val doc = dbf.newDocumentBuilder().parse(file)

        val processNode = doc.getElementsByTagName("semantic:process").item(0)

        process = CProcess(processNode.attributes.getNamedItem("name").nodeValue, uuid)

        val processChildList = processNode.childNodes

        val processItemMap = getProcessObjectsMap(processChildList)

        addConnections(processItemMap)

        makeProcess(process, processItemMap)

    }catch(e: Exception) {
        println("Error: " + e.message)
    }

    return process
}

fun main(args: Array<String>) {

    val file = File("C:\\Users\\cepeh\\OneDrive\\Рабочий стол\\sel.bpmn")

    val process = parseBPMN(UUID.randomUUID(), file)



}