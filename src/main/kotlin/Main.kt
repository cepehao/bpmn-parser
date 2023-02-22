import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory

fun getEvent(curNode: Node, eventType: EEventType): CEvent {
    val nodeList = curNode.childNodes

    val incomingList = arrayListOf<String>()
    val outgoingList = arrayListOf<String>()

    for (i:Int in 0..nodeList.length - 1) {
        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue

        if (nodeList.item(i).nodeName == "semantic:incoming") {
            incomingList.add(nodeList.item(i).textContent.substring(3))
        }

        if (nodeList.item(i).nodeName == "semantic:outgoing") {
            outgoingList.add(nodeList.item(i).textContent.substring(3))
        }
    }

    return CEvent(curNode.attributes.getNamedItem("name").nodeValue, eventType, incomingList, outgoingList)
}

fun getGateway(curNode: Node, gatewayType: EGateway): CGateway { // todo правило выбора
    val nodeList = curNode.childNodes

    val incomingList = arrayListOf<String>()
    val outgoingList = arrayListOf<String>()

    for (i:Int in 0..nodeList.length - 1) {
        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue

        if (nodeList.item(i).nodeName == "semantic:incoming") {
            incomingList.add(nodeList.item(i).textContent.substring(3))
        }

        if (nodeList.item(i).nodeName == "semantic:outgoing") {
            outgoingList.add(nodeList.item(i).textContent.substring(3))
        }
    }

    return CGateway(curNode.attributes.getNamedItem("name").nodeValue, gatewayType, incomingList, outgoingList)
}

fun getTask(curNode: Node): CTask {
    val nodeList = curNode.childNodes

    val incomingList = arrayListOf<String>()
    val outgoingList = arrayListOf<String>()

    for (i:Int in 0..nodeList.length - 1) {
        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue

        if (nodeList.item(i).nodeName == "semantic:incoming") {
            incomingList.add(nodeList.item(i).textContent.substring(3))
        }

        if (nodeList.item(i).nodeName == "semantic:outgoing") {
            outgoingList.add(nodeList.item(i).textContent.substring(3))
        }
    }

    return CTask(curNode.attributes.getNamedItem("name").nodeValue, incomingList, outgoingList)
}

// создаем карту всех процессов, вместе со строковыми идентификаторами связей
fun getProcessObjectsMap(nodeList: NodeList): MutableMap<String, CProcessItem> {
    var node: Node
    var processObjectsMap = mutableMapOf<String, CProcessItem>()

    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node == null || node.nodeType != Node.ELEMENT_NODE) continue

        when(node.nodeName) {
            "semantic:startEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                                getEvent(node, EEventType.START))
            }

            "semantic:task" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                                getTask(node))
            }

            "semantic:endEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    getEvent(node, EEventType.END))
            }

            "semantic:intermediateCatchEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    getEvent(node, EEventType.INTERMEDIATE))
            }

            "semantic:exclusiveGateway" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    getGateway(node, EGateway.EXCLUSIVE))
            }
        }
    }

    return processObjectsMap
}

//проверить список строковых идентификаторов на входы/выходы в объекты процесса, вернуть список объектов процесса
fun createIncomingList (items: MutableMap<String, CProcessItem>, incomingIdList: ArrayList<String>): ArrayList<CProcessItem?> {
    var incomingList = arrayListOf<CProcessItem?>()

    for (id in incomingIdList) {
        for (item in items.values) {
            if (item.checkOutgoing(id)) incomingList.add(item)
        }
    }
    return incomingList
}

fun createOutgoingList (items: MutableMap<String, CProcessItem>, outgoingIdList: ArrayList<String>): ArrayList<CProcessItem?> {
    var outgoingList = arrayListOf<CProcessItem?>()

    for (id in outgoingIdList) {
        for (item in items.values) {
            if (item.checkIncoming(id)) outgoingList.add(item)
        }
    }
    return outgoingList
}

fun addConnections(items: MutableMap<String, CProcessItem>) {

    var incomingIdList = arrayListOf<String>()
    var outgoingIdList = arrayListOf<String>()
    var incomingList = arrayListOf<CProcessItem?>()
    var outgoingList = arrayListOf<CProcessItem?>()

    for (i in items.entries.iterator()) {
        incomingIdList = i.value.getIncomingIdList() // получили строковые идентификаторы связей из i-ого объекта
        outgoingIdList = i.value.getOutgoingIdList()

        incomingList = createIncomingList(items, incomingIdList)
        outgoingList = createOutgoingList(items, outgoingIdList)

        i.value.incomingList = incomingList
        i.value.outgoingList = outgoingList
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

        var processObjectsMap = getProcessObjectsMap(processChildList)


        addConnections(processObjectsMap)

        var x = 5
        //var processChild: Node


//        for (i:Int in 0..processChildList.length - 1) {
//            processChild = processChildList.item(i)
//
//            if (processChild == null || processChild.nodeType != Node.ELEMENT_NODE) continue
//
//            when(processChild.nodeName) {
//                "semantic:startEvent" -> {
//                    process.addEvent(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
//                                    getEvent(processChild, EEventType.START))
//                }
//
//                "semantic:task" -> {
//                    process.addTask(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
//                                    getTask(processChild))
//                }
//
//                "semantic:endEvent" -> {
//                    process.addEvent(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
//                        getEvent(processChild, EEventType.END))
//                }
//
//                "semantic:intermediateCatchEvent" -> {
//                    process.addEvent(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
//                        getEvent(processChild, EEventType.INTERMEDIATE))
//                }
//
//                "semantic:exclusiveGateway" -> {
//                    process.addGateway(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
//                                    getGateway(processChild, EGateway.EXCLUSIVE))
//                }
//            }
//        }
    }catch(e: Exception) {
        println("Error: " + e.message)
    }

    return process
}


fun main(args: Array<String>) {

    val file = File("C:\\Users\\cepeh\\OneDrive\\Рабочий стол\\sel.bpmn")

    val process = parseBPMN(UUID.randomUUID(), file)

}