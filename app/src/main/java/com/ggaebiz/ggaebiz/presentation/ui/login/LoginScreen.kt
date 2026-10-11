package com.ggaebiz.ggaebiz.presentation.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ggaebiz.ggaebiz.R
import com.ggaebiz.ggaebiz.data.auth.KakaoLoginHandler
import com.ggaebiz.ggaebiz.presentation.common.extension.collectSideEffectWithLifecycle
import com.ggaebiz.ggaebiz.presentation.designsystem.theme.GaeBizTheme
import com.ggaebiz.ggaebiz.presentation.designsystem.ui.FullScreen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    navigateHome: () -> Unit,
    navigateOnboarding: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(),
    kakaoLoginHandler: KakaoLoginHandler = koinInject(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    viewModel.sideEffects.collectSideEffectWithLifecycle { effect ->
        when (effect) {
            LoginSideEffect.NavigateHome -> navigateHome()
            LoginSideEffect.NavigateOnboarding -> navigateOnboarding()
            LoginSideEffect.RequestKakaoLogin -> {
                scope.launch {
                    val result = kakaoLoginHandler.login(context)
                    viewModel.processIntent(LoginIntent.OnLoginResult(result))
                }
            }
            is LoginSideEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoginContent(
        processIntent = viewModel::processIntent,
    )
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    processIntent: (LoginIntent) -> Unit,
) {
    FullScreen(
        backGroundGradient = GaeBizTheme.colors.gradientLightOrange,
        backgroundColor = GaeBizTheme.colors.primaryOrange50,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            
            Column(
                modifier = Modifier.height(540.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.login_title_text),
                    color = GaeBizTheme.colors.gray900,
                    style = GaeBizTheme.typography.titleBold,
                    modifier = modifier.fillMaxWidth().align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.login_subtitle_text),
                    color = GaeBizTheme.colors.gray700,
                    style = GaeBizTheme.typography.bodyMedium.copy(fontSize = 20.sp),
                    modifier = modifier.fillMaxWidth().align(Alignment.Start),
                )

                Spacer(modifier = Modifier.height(22.dp))

                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.login_illust_area),
                    contentDescription = null,
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { processIntent(LoginIntent.ClickLoginButton) },
                    painter = painterResource(R.drawable.kakao_login_button),
                    contentDescription = null,
                )
            }
        }
    }
}
