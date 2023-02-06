import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import java.rmi.server.UID
import javax.xml.parsers.DocumentBuilderFactory

fun parseBPMN(uid: UID, path: String){
    try {
        val file = File(path)
        val dbf = DocumentBuilderFactory.newInstance()
        val doc = dbf.newDocumentBuilder().parse(file)

        val processNode = doc.getElementsByTagName("semantic:process").item(0)

        val process = CProcess(processNode.attributes.getNamedItem("name").nodeValue, uid)

        val processChildList = processNode.childNodes
        var processChild: Node
        var outgoingList = arrayListOf<String>()
        var nodeList: NodeList

        for (i:Int in 0..processChildList.length - 1) {
            processChild = processChildList.item(i)

            if (processChild == null || processChild.nodeType != Node.ELEMENT_NODE) continue

            when(processChild.nodeName) {
                "semantic:startEvent" -> {
                    process.setStartEvent(
                        CStartEvent(processChild.attributes.getNamedItem("name").nodeValue,
                            processChild.attributes.getNamedItem("id").nodeValue.substring(3),
                            processChild.childNodes.item(1).textContent.substring(3)))
                }

                "semantic:task" -> {
                    process.addTask(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
                        CTask(processChild.attributes.getNamedItem("name").nodeValue,
                            processChild.childNodes.item(1).textContent.substring(3),
                            processChild.childNodes.item(3).textContent.substring(3)))
                }

                "semantic:endEvent" -> {
                    process.addEndEvent(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
                        CEndEvent(processChild.attributes.getNamedItem("name").nodeValue,
                            processChild.childNodes.item(1).textContent.substring(3)))
                }

                "semantic:intermediateCatchEvent" -> {
                    process.addIntermediateCatchEvent(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
                        CIntermediateCatchEvent(processChild.attributes.getNamedItem("name").nodeValue,
                            processChild.childNodes.item(1).textContent.substring(3),
                            processChild.childNodes.item(3).textContent.substring(3)))
                }

                "semantic:exclusiveGateway" -> {
                    nodeList = processChild.childNodes

                    for (i:Int in 2..nodeList.length - 1) {
                        if (nodeList.item(i).nodeType != Node.ELEMENT_NODE) continue
                        outgoingList.add(nodeList.item(i).textContent.substring(3))
                    }
                    process.addExclusiveGateway(processChild.attributes.getNamedItem("id").nodeValue.substring(3),
                        CExclusiveGateway(processChild.attributes.getNamedItem("name").nodeValue,
                            processChild.childNodes.item(1).textContent.substring(3),
                            outgoingList))
                }
            }
        }

    }catch(e: Exception) {
        println("Error: " + e.message)
    }
}
fun main(args: Array<String>) {
    parseBPMN(UID(), "C:\\Users\\cepeh\\OneDrive\\Рабочий стол\\sel.bpmn")
}