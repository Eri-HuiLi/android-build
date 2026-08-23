package com.operit.intro

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val avatar = findViewById<MaterialTextView>(R.id.tvAvatar)
        val title = findViewById<MaterialTextView>(R.id.tvTitle)
        val subtitle = findViewById<MaterialTextView>(R.id.tvSubtitle)

        // 入场动画：淡入 + 上浮，依次出现
        listOf(avatar, title, subtitle).forEachIndexed { index, view ->
            view.alpha = 0f
            view.translationY = 40f
            view.animate().alpha(1f).translationY(0f)
                .setStartDelay(150L * index)
                .setDuration(500L)
                .start()
        }

        val cards = listOf(
            findViewById<MaterialCardView>(R.id.card1) to "🛠️ 终端、代码、UI 自动化……说吧，想让我干点啥",
            findViewById<MaterialCardView>(R.id.card2) to "☁️ 刚才这个 APP 就是云端编译的，不服再点一下",
            findViewById<MaterialCardView>(R.id.card3) to "🧠 我记得你喜欢轻松的聊天氛围哦",
            findViewById<MaterialCardView>(R.id.card4) to "🎨 心情卡片就是我的表情包库"
        )

        cards.forEachIndexed { index, (card, message) ->
            card.alpha = 0f
            card.translationY = 60f
            card.animate().alpha(1f).translationY(0f)
                .setStartDelay(300L + 120L * index)
                .setDuration(450L)
                .start()

            card.setOnClickListener {
                Snackbar.make(it, message, Snackbar.LENGTH_SHORT)
                    .setAnchorView(R.id.fabWish)
                    .show()
            }
        }

        findViewById<ExtendedFloatingActionButton>(R.id.fabWish).setOnClickListener {
            Toast.makeText(this, "愿望已签收！回聊天里告诉我吧 ✨", Toast.LENGTH_LONG).show()
        }
    }
}