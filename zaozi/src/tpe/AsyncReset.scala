// SPDX-License-Identifier: Apache-2.0

package me.jiuyang.zaozi

object AsyncReset:
  def apply(): AsyncReset = new AsyncReset
class AsyncReset extends Data:
  def firrtlType = new firrtl.AsyncReset(false)

trait AsAsyncReset[D <: Data, R <: Referable[D]]:
  extension (ref: R)
    def asAsyncReset(
      using ctx: Context,
      file:      sourcecode.File,
      line:      sourcecode.Line,
      valName:   sourcecode.Name
    ): Node[AsyncReset]
