package com.myco.comet

import java.util.Date
import java.util.concurrent.TimeUnit

import net.liftweb.common.Full
import net.liftweb.http.CometActor
import net.liftweb.util.CanBind._
import net.liftweb.util.TimeHelpers
import net.liftweb.actor.LAPinger
import net.liftweb.util.TimeHelpers._
import net.liftweb.http.js.JsCmds._
import scala.xml.{ Text }

import scala.concurrent.duration.Duration

class Clock extends CometActor {
  override def defaultPrefix = Full("clk")

  def render = "#time" #> timeSpan

  def timeSpan = (<span id="time">{now}</span>)

  // schedule a ping every 10 seconds so we redraw
  LAPinger.schedule(this, Tick, 10000L)

  override def lowPriority : PartialFunction[Any, Unit] = {
    case Tick => {
      println("Got tick " + new Date());
      partialUpdate(SetHtml("time", Text(now.toString)))
      // schedule an update in 10 seconds
      LAPinger.schedule(this, Tick, 10000L)
    }
  }
}
case object Tick
