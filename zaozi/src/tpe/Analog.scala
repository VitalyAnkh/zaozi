// SPDX-License-Identifier: Apache-2.0
package me.jiuyang.zaozi

object Analog:
  def apply(width: Width): Analog = new Analog(width)
class Analog(width: Width) extends Data:
  def firrtlType = new firrtl.Analog(width.toInt, false)
