package com.unnakim.midiconvdaemon

import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*

import kotlin.concurrent.thread
import java.util.Vector

import de.tobiaserichsen.tevm.TeVirtualMIDI
import de.tobiaserichsen.tevm.TeVirtualMIDIException
import java.lang.Thread.sleep
import javax.sound.midi.*

fun main(args: Array<String>) {
    val server = ServerSocket(9999)
    var vMidi: TeVirtualMIDI = TeVirtualMIDI("MidiConvDaemonVirtualMidi", 66555, TeVirtualMIDI.TE_VM_FLAGS_INSTANTIATE_BOTH)
    TeVirtualMIDI.logging(TeVirtualMIDI.TE_VM_LOGGING_MISC or TeVirtualMIDI.TE_VM_LOGGING_RX or TeVirtualMIDI.TE_VM_LOGGING_TX)

    var winMidi: MidiDevice = getVirtualMidiDevice()

    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")

        // Run client in it's own thread.
        thread { ClientHandler(client, vMidi).run() }
    }

}

fun translateIntsToMidiByte(select: Int, data1: Int, data2: Int): ByteArray {
    println("translate $select, $data1, $data2 ...")
    println((select % 128).toByte() + (select / 128).toByte() * 128)
    println(data1.toByte())
    println(data2.toByte())
    val result: ByteArray = byteArrayOf((select).toByte() , data1.toByte(), data2.toByte())
    for (b in result) {
        val st = String.format("%02X", b)
        print(st)
    }
    println("\nbyte translated.")
    return result
}

fun getVirtualMidiDevice(): MidiDevice {
    val infos = MidiSystem.getMidiDeviceInfo()
    var device: MidiDevice = MidiSystem.getMidiDevice(infos[0])
    println("Listing MIDI Device...: ")

    for (i in infos.indices) {
        try {
            device = MidiSystem.getMidiDevice(infos[i])
            println("===== Device Info =====\n ${device.deviceInfo.name}, ${device.maxReceivers}, ${device.maxTransmitters}, ${device.deviceInfo.description}")

            if (device.deviceInfo.name == "MidiConvDaemonVirtualMidi") {
                break
            }
        } catch (e: MidiUnavailableException) {
            // Handle or throw exception...
        }
    }
    println("Virtual Midi Device ${device.deviceInfo.name}(Version: ${device.deviceInfo.version}) found.")

    return device
}

class MidiTransMitter(device: MidiDevice) {
    private val midiDevice: MidiDevice = device

    fun sendMidi(channel: Int, data1: Int, data2: Int) {
        val myMsg = ShortMessage()
        myMsg.setMessage(ShortMessage.NOTE_ON, channel, data1, data2)
        val timeStamp: Long = -1
        try {
            val rcvr = midiDevice.getReceiver()
            rcvr.send(myMsg, timeStamp)
        } catch (e: Exception) {
            println("Got Error while transmitting MIDI: ${e.javaClass.name} ${e.message}, device: ${midiDevice.deviceInfo.name}")
        }

    }
}

class ClientHandler(client: Socket, device: TeVirtualMIDI) {
    private val client: Socket = client
    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val midiPortDevice: TeVirtualMIDI = device

    fun run() {
        try {
            val text = reader.nextLine().trim()
            println("text: $text")
            sendMidiCommend(text, midiPortDevice)
            client.close()

            println("${client.inetAddress.hostAddress} closed the connection")

        } catch (e: Exception) {
            println("Got Error while Handling Message: ${e.message}")

            client.close()
        } finally {

        }
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

}

fun sendMidiCommend(text: String, device: TeVirtualMIDI){
    if (text != "" && text[0] == 'M'){
        try {
            var midinote: Int = text.split('M')[1].toInt()

            device.sendCommand( translateIntsToMidiByte(144, midinote, 120))
            sleep(50)
            device.sendCommand( translateIntsToMidiByte(128, midinote, 120))
        }
        catch (e: Exception) {
            println("Error occoured ($e) when printing Notes from $text")
        }

        println("MIDI Trnasmit Complete from input ASCII $text")
    }
}


//
//class AsciiTranslator(translationMap: Map<String, String>) {
//    private val asciiToMidiMap: Map<String, String> = translationMap
//    fun translate(ascii: String) {
//        try {
//        } catch (ex: Exception) {
//            println("error occured while translating ASCII ")
//        }
//    }
//}