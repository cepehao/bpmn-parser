import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


fun main(args: Array<String>) {
    val path = "" // .bpmn файл

    try {
        val file = File(path)
        val dbf = DocumentBuilderFactory.newInstance()
        val doc = dbf.newDocumentBuilder().parse(file)


        val processNode = doc.getElementsByTagName("semantic:process").item(0)

        val process = CProcess(processNode.attributes.getNamedItem("name").nodeValue,
                                processNode.attributes.getNamedItem("id").nodeValue.substring(11))

        val processChildList = processNode.childNodes


        for (i:Int in 0..processChildList.length - 1) {
            val processChild = processChildList.item(i)

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
                    var outgoingList = arrayListOf<String>()
                    val nodeList = processChild.childNodes

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