package com.example.showtimeoutduration

import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.aliucord.api.CommandsAPI.CommandResult
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType
import com.discord.widgets.chat.input.ChatInputViewModel
import com.discord.stores.StoreStream
import com.discord.utilities.time.ClockFactory
import java.util.concurrent.TimeUnit

class ShowTimeoutDuration : Plugin() {
    override fun start(ctx: Context) {
        // Listen for timeout actions
        patcher.patch(
            ChatInputViewModel::class.java.getDeclaredMethod(
                "handleTimeoutAction",
                Long::class.javaPrimitiveType
            ),
            Hook { callFrame ->
                val userId = callFrame.args[0] as Long
                val timeoutDuration = getTimeoutDurationForUser(userId)
                
                val timeoutMessage = "User has been timed out for $timeoutDuration seconds."
                showToast(timeoutMessage)
            }
        )
    }

    override fun stop(ctx: Context) {
        patcher.unpatchAll()
    }

    // Mock function to return a timeout duration
    private fun getTimeoutDurationForUser(userId: Long): Long {
        // Fetch the actual timeout duration if available (mocked here)
        return TimeUnit.HOURS.toSeconds(1) // Example: 1-hour timeout
    }

    // Show the toast with the timeout message
    private fun showToast(message: String) {
        StoreStream.getContext().let { context ->
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG).show()
        }
    }
}
