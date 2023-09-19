package com.moneyminions.presentation.screen.game

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.moneyminions.presentation.common.CustomTextStyle
import com.moneyminions.presentation.common.MinionPrimaryButton
import com.moneyminions.presentation.common.TopBar
import com.moneyminions.presentation.theme.PinkDarkest
import com.moneyminions.presentation.theme.PinkLight
import com.moneyminions.presentation.theme.PinkLightest
import com.moneyminions.presentation.viewmodel.game.WordGameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordGameScreen(
    navController: NavHostController,
    wordGameViewModel: WordGameViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(navController = navController, title = "초성게임")
        },
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(wordGameViewModel.isShowWord.value){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(128.dp) // 원하는 크기 설정
                                .background(PinkLightest, shape = RoundedCornerShape(16.dp))
                        ){
                            Text(
                                text = wordGameViewModel.firstConsonant.value,
                                style = CustomTextStyle.pretendardExtraBold64,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        Spacer(modifier = Modifier.size(16.dp))
                        Box(
                            modifier = Modifier
                                .size(128.dp) // 원하는 크기 설정
                                .background(PinkLightest, shape = RoundedCornerShape(16.dp))
                        ){
                            Text(
                                text = wordGameViewModel.secondConsonant.value,
                                style = CustomTextStyle.pretendardExtraBold64,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }else{
//                    Box(modifier = Modifier.size(128.dp).background(PinkDarkest))
                }
                Spacer(modifier = Modifier.size(16.dp))
                MinionPrimaryButton(
                    content = getButtonContent(),
                    modifier = Modifier
                ) {
                    wordGameViewModel.setIsShowWord(false)
                    scope.launch {
                        delay(3000) // 3초 지연
                        wordGameViewModel.setIsShowWord(true)
                    }
                    wordGameViewModel.setFirstConsonant()
                    wordGameViewModel.setSecondConsonant()
                }
            }

        }
    }
}

@Composable
fun getButtonContent(
    wordGameViewModel: WordGameViewModel = hiltViewModel()
): String {
    return if (wordGameViewModel.firstConsonant.value.isEmpty()) {
        "시작"
    } else {
        "다시"
    }
}

@Preview(showBackground = true)
@Composable
fun WordGameScreenPreview(){
    WordGameScreen(navController = rememberNavController())
}