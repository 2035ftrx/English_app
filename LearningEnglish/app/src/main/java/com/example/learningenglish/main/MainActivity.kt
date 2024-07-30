package com.example.learningenglish.main

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.learningenglish.home.HomeScreen
import com.example.learningenglish.setting.MineScreen
import com.example.learningenglish.word.statistics.StatisticsScreen
import com.example.learningenglish.ui.base.BaseComposeActivity
import com.example.learningenglish.ui.view.EnableEdgeLight
import com.example.learningenglish.user.LauncherActivity

class MainActivity : BaseComposeActivity() {

    @Composable
    override fun RenderContent() {
        EnableEdgeLight()

        Column {

            val tab = remember { mutableStateOf<IMainTabs>(IMainTabs.Home) }

            val tabs = listOf(
                IMainTabs.Home,
                IMainTabs.Statistics,
                IMainTabs.Setting
            )

            Box(modifier = Modifier.weight(1f).statusBarsPadding()) {
                when (tab.value) {
                    IMainTabs.Home -> {
                        HomeScreen(Modifier.fillMaxSize())
                    }

                    IMainTabs.Statistics -> {
                        StatisticsScreen(modifier = Modifier.fillMaxSize())
                    }

                    IMainTabs.Setting -> {
                        MineScreen(modifier = Modifier.fillMaxSize(), onLogout = {
                            context.startActivity(Intent(context, LauncherActivity::class.java))
                            finish()
                        })
                    }
                }
            }

            NavigationBar(Modifier.fillMaxWidth()) {
                tabs.forEach {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = it.icon()),
                                contentDescription = it.title()
                            )
                        },
                        label = { Text(it.title()) },
                        selected = tab.value == it,
                        onClick = {
                            tab.value = it
                        }
                    )
                }
            }

        }


    }
}
