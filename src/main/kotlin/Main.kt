import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory

//fun getEvent(curNode: Node, eventType: EEventType): CEvent {
//    val nodeList = curNode.childNodes
//
//    val incomingList = arrayListOf<CIncoming>()
//    val outgoingList = arrayListOf<COutgoing>()
//
//    for (i:Int in 0..nodeList.length - 1) {
//        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue
//
//        if (nodeList.item(i).nodeName == "semantic:incoming") {
//            incomingList.add(CIncoming(nodeList.item(i).textContent.substring(3)))
//        }
//
//        if (nodeList.item(i).nodeName == "semantic:outgoing") {
//            outgoingList.add(COutgoing(nodeList.item(i).textContent.substring(3)))
//        }
//    }
//
//    return CEvent(curNode.attributes.getNamedItem("name").nodeValue, incomingList, outgoingList, eventType)
//}
//
//fun getGateway(curNode: Node, gatewayType: EGateway): CGateway { // todo правило выбора
//    val nodeList = curNode.childNodes
//
//    val incomingList = arrayListOf<CIncoming>()
//    val outgoingList = arrayListOf<COutgoing>()
//
//    for (i:Int in 0..nodeList.length - 1) {
//        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue
//
//        if (nodeList.item(i).nodeName == "semantic:incoming") {
//            incomingList.add(CIncoming(nodeList.item(i).textContent.substring(3)))
//        }
//
//        if (nodeList.item(i).nodeName == "semantic:outgoing") {
//            outgoingList.add(COutgoing(nodeList.item(i).textContent.substring(3)))
//        }
//    }
//
//    return CGateway(curNode.attributes.getNamedItem("name").nodeValue, incomingList, outgoingList, gatewayType)
//}
//
//fun getTask(curNode: Node): CTask {
//    val nodeList = curNode.childNodes
//
//    val incomingList = arrayListOf<CIncoming>()
//    val outgoingList = arrayListOf<COutgoing>()
//
//    for (i:Int in 0..nodeList.length - 1) {
//        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue
//
//        if (nodeList.item(i).nodeName == "semantic:incoming") {
//            incomingList.add(CIncoming(nodeList.item(i).textContent.substring(3)))
//        }
//
//        if (nodeList.item(i).nodeName == "semantic:outgoing") {
//            outgoingList.add(COutgoing(nodeList.item(i).textContent.substring(3)))
//        }
//    }
//
//    return CTask(curNode.attributes.getNamedItem("name").nodeValue, incomingList, outgoingList)
//}

fun getProcessObjectsMap(nodeList: NodeList): MutableMap<String, CProcessObject> {
    var node: Node
    var processObjectsMap = mutableMapOf<String, CProcessObject>()

    for (i:Int in 0..nodeList.length - 1) {
        node = nodeList.item(i)

        if (node == null || node.nodeType != Node.ELEMENT_NODE) continue

        when(node.nodeName) {
            "semantic:startEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                                CEvent(node.attributes.getNamedItem("name").nodeValue, EEventType.START))
            }

            "semantic:task" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    CTask(node.attributes.getNamedItem("name").nodeValue))
            }

            "semantic:endEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    CEvent(node.attributes.getNamedItem("name").nodeValue, EEventType.END))
            }

            "semantic:intermediateCatchEvent" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    CEvent(node.attributes.getNamedItem("name").nodeValue, EEventType.INTERMEDIATE))
            }

            "semantic:exclusiveGateway" -> {
                processObjectsMap.put(node.attributes.getNamedItem("id").nodeValue.substring(3),
                    CGateway(node.attributes.getNamedItem("name").nodeValue, EGateway.EXCLUSIVE))
            }
        }
    }

    return processObjectsMap
}

fun addConnections(process: CProcess, processObjectMap: MutableMap<String, CProcessObject>, processChildList: NodeList) {
    val listobjectNames = arrayListOf<String>("semantic:startEvent", "semantic:task", "semantic:endEvent", "semantic:intermediateCatchEvent", "semantic:exclusiveGateway")

    var processChild: Node

    for (i:Int in 0..processChildList.length - 1) {
        processChild = processChildList.item(i)

        if (processChild == null || processChild.nodeType != Node.ELEMENT_NODE || processChild.nodeName !in listobjectNames) continue




        val nodeList = processChild.childNodes

        var id: String = ""
        var incomingList = arrayListOf<CProcessObject?>()
        var outgoingList = arrayListOf<CProcessObject?>()

        for (i:Int in 0..nodeList.length - 1) {
            if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue

            id = nodeList.item(i).textContent.substring(3)


            if (nodeList.item(i).nodeName == "semantic:incoming") {

                incomingList.add(processObjectMap.get(id))
            }

            if (nodeList.item(i).nodeName == "semantic:outgoing") {
                outgoingList.add(processObjectMap.get(id))
            }


        }
        processObjectMap[id]?.addConnectionList(incomingList, outgoingList)



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

        addConnections(process, processObjectsMap, processChildList)


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