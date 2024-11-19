package com.omasyo.gatherspace.createroom

import com.github.sarxos.webcam.*
import java.awt.BorderLayout
import java.awt.event.ItemEvent
import java.awt.event.ItemListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.lang.Thread.UncaughtExceptionHandler
import javax.swing.JFrame
import javax.swing.SwingUtilities


class WebcamViewerExample : JFrame(), Runnable, WebcamListener, WindowListener, UncaughtExceptionHandler,
    ItemListener, WebcamDiscoveryListener {
    private var webcam: Webcam? = null
    private var panel: WebcamPanel? = null
    private var picker: WebcamPicker? = null

    override fun run() {
        Webcam.addDiscoveryListener(this)

        title = "Java Webcam Capture POC"
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        addWindowListener(this)

        picker = WebcamPicker()
        picker!!.addItemListener(this)

        webcam = picker!!.selectedWebcam

        if (webcam == null) {
            println("No webcams found...")
            System.exit(1)
        }

        webcam!!.setViewSize(WebcamResolution.VGA.size)
        webcam!!.addWebcamListener(this@WebcamViewerExample)

        panel = WebcamPanel(webcam, false)
        panel!!.isFPSDisplayed = true

        add(picker, BorderLayout.NORTH)
        add(panel, BorderLayout.CENTER)

        pack()
        isVisible = true

        val t: Thread = object : Thread() {
            override fun run() {
                panel!!.start()
            }
        }
        t.name = "example-starter"
        t.isDaemon = true
        t.uncaughtExceptionHandler = this
        t.start()
    }

    override fun webcamOpen(we: WebcamEvent) {
        println("webcam open")
    }

    override fun webcamClosed(we: WebcamEvent) {
        println("webcam closed")
    }

    override fun webcamDisposed(we: WebcamEvent) {
        println("webcam disposed")
    }

    override fun webcamImageObtained(we: WebcamEvent) {
        // do nothing
    }

    override fun windowActivated(e: WindowEvent) {
    }

    override fun windowClosed(e: WindowEvent) {
        webcam!!.close()
    }

    override fun windowClosing(e: WindowEvent) {
    }

    override fun windowOpened(e: WindowEvent) {
    }

    override fun windowDeactivated(e: WindowEvent) {
    }

    override fun windowDeiconified(e: WindowEvent) {
        println("webcam viewer resumed")
        panel!!.resume()
    }

    override fun windowIconified(e: WindowEvent) {
        println("webcam viewer paused")
        panel!!.pause()
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        System.err.println(String.format("Exception in thread %s", t.name))
        e.printStackTrace()
    }

    override fun itemStateChanged(e: ItemEvent) {
        if (e.item !== webcam) {
            if (webcam != null) {
                panel!!.stop()

                remove(panel)

                webcam!!.removeWebcamListener(this)
                webcam!!.close()

                webcam = e.item as Webcam
                webcam!!.viewSize = WebcamResolution.VGA.size
                webcam!!.addWebcamListener(this)

                println("selected " + webcam!!.name)

                panel = WebcamPanel(webcam, false)
                panel!!.isFPSDisplayed = true

                add(panel, BorderLayout.CENTER)
                pack()

                val t: Thread = object : Thread() {
                    override fun run() {
                        panel!!.start()
                    }
                }
                t.name = "example-stoper"
                t.isDaemon = true
                t.uncaughtExceptionHandler = this
                t.start()
            }
        }
    }

    override fun webcamFound(event: WebcamDiscoveryEvent) {
        if (picker != null) {
            picker!!.addItem(event.webcam)
        }
    }

    override fun webcamGone(event: WebcamDiscoveryEvent) {
        if (picker != null) {
            picker!!.removeItem(event.webcam)
        }
    }
}