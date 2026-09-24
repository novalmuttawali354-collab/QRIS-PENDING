package com.citawin.autorekap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

public class MainActivity extends Activity {
 private static final String START_URL="https://citawin.idrbo2.com/historical-knowledge.html";
 private static final String SCRIPT_B64="H4sIAM+RtGoC/+197W4bSZLgfz5F2o0Zim2RIqkPS1TLvbRF22xJlEai2vJ6vNokK0mWVaxiVxUtadwC5sfd/jgsMNjdAQ5Y3GLv" +
            "EQ64P4d7nHmB20e4/KpiVVZmVRZZavcAq8G0pcqvyIjIyIjIyMiNDXBwcOkh92LomjP/4KC0sQH+xoZTBIKfV91++123B9pnR6B9" +
            "2T8F552j9llYz5vBIa08NH14a9o1OPcdWvoZuZ7p2KyXZq1ehbMbWmAgj45GC8PubcN1TAO8cqa4Rx+cuQ6ogvbxMfjdefcCPANn" +
            "yDZMewxt/PtR+wI08L99x4cWaM9mrvMZ0b6n0B9OAtAnvj/zWhsbAWim4Q6cZm3oTDe+VdeGxhTX5W286SBSf+xC2w/qvzm5vpta" +
            "pNU5+mmOPJ9WGTq2jYa8Eptnbew4YwuRfjKqzDElcKmPbD+s7c7tKgwGNZzhfIpLq6Zh0QkfHGzEyFcqrY3m9pDidq0CvpRKpFkZ" +
            "dww83zWHfnm/xL7hcTwfWM4QWhe+48IxAge4fjC7MfK7Ppq2wKK7G3SPewS+e4//y5p/PuB0e+maxhjVeKu1CzyWPaYtKvvARf7c" +
            "tXHlg4Py9TWn+HXv8vj4+rr8vT23rNbnffAAhpQca9dkFN6GFJKih/UQMk8K2Tr4DK05WgAYB8xLArYO+B+sYUWAIDami6aYw1IR" +
            "Eh9v0UDAhTgGHeJhP0qT62vOfD3om58R5y6PkAdXJPVwmeHc1hIVvRnugNBxAaPLWneNdeDcrFMmv/ChP/fW8aRY/T668ysR0jMo" +
            "HLpAvQMFNB/Cjj/uA3O09oTXrzDC7QMDWchHQKN5ODBuAGbQ9ZBxwOiOMfuFf4gC+/0PF6e9Gi1Yi02iRZs9UAxjBH95EOYUVD74" +
            "4lEctHrz6QC5awus/PxzvbIe7bPFyRf99vPP5fKiVotB+LCYBwGb4MS5qdB//fsZckYBRmuObTnQIIshIFO5Ei8LR6MMgyxMU1k3" +
            "yHUdV9UPLYx1xNBCv1e+EIQ4WCixauzjfsiN5J+QhxKCbi2gdYJpQqoelIf+7XX52SH0Uc12btcqz8r4zxPoT2pYiBrOdK1S8x2O" +
            "3M2dSs2zzCFaa2JAs1nmgAPw889fBLTHV+HM8fzXjhuuwQDw3/42wNPctSqMnMoqBvRhJSA5h6CyH8emPrNTvgiGWIaiAevWpWzK" +
            "4HkIScmF/ca3S/4w5H4L3rR7/S64PD8G3V4XHHZ6+AP9cwPdoSFWDs4uwMWr8+5ZH2/OJ5e82ZI/G1Fp+K7z8hp3f00GOwhpXQ52" +
            "68QWuzGFQ9fxNryN9tHofji4PffGvT8M22P4Zvbjae/t9mxerxqfric/nA8PJ5ZrWH970vvDzeYIb78jZzrvj3fv5hfW5fbleLd6" +
            "W9/8fHj0OzrJcOdcGZk/tBn6Xrbfgv7pUadXILpof1FE8e22etbpHXbJuNVmvbkjqAEYuddvuxf90/P3MiRLFKiJ6WGtwcTaQ/UG" +
            "r28LkfU28aeWpOf+ebt30X7V7572NHu30W3Vx3LCg3QVqDo+bHeP31+/6/bfHp6338n6lipzLpo5rr9hQNO6vzWEfo8672WoCLQW" +
            "XnR9dNrtSVqedHud87asISu5JtqspB35fH3Wft886b6VtY6Wq5qfnmME4zrK9kEFVQdXaY2vUhoe4Yr988uLvrJ1WEPShYx+QeOg" +
            "7Lp/2m8fSxpTk0TSkHy/ZjwnacUXw/Xhaa8jax0tlzRnfHfeeX3euXh7fYZ/6V7JuqH13h0GNTuH1zJY2r3O8fXZ6YUUkKCwq5oJ" +
            "rYCZS90aF3ZPun/bOaTNixFhtOcCxdYM2gjbIfcWiswjsHZqQxdhPaJjIfLXWlhO5+qRRuXwWyVA0aLHmo83yFfMrMK68d+Xwsrf" +
            "cMHQxjbrGakfUWhoH45nEvmDlX7zDhn7sULfmbXA7u7sLv7ZNccTvwUaiYJb0/AnLbCZLJnCuyovxfJ0uNao1z/fYuu3uTW7q8Sr" +
            "Dh3LcVvgGwRHw9EoXjbCU6yO4BRLtRbo4um666DtmtBaBx4WpVVsJ5pCkz9UsSWB7lqg2dh6vrW7ubP1PF5h4LgGcqsuNEysc4Bm" +
            "XYQdWznuyHJuW2BiGgayZc0xNmZ3AKub2L53xwO49nx3HTSbe/g/29vroNbcEWY5gMObsevMbaMV+07xi0HBm86Y/Eu4YWi6Q8w1" +
            "2ETGBMEmxMhfZ4PU15vN3XUyQq2BlXpA9xOsqxMuaO7+prKe6NoybQTdRdeN3bqBxry7zfXm5vpmc72297zCP22vb22ub+/ST+IM" +
            "nLuqN4HYSkvOoE6ZA2xhXAaQkv/VsBa8LqlM/tcIqpLpBP+v1bcqAO9qyJc3a+4uBmg2nnNUbElwjXXmWXVkWj6h1cCau5gFE6xH" +
            "nBOYhyw0xOxtY3V0UfyQtqS+FRYVRY35B6yttgL2wp80O6ths6JKl3Z1jLlOuV7hALPb3EfxKVBktQDWgH4DiJ+K/afaqP9GulQb" +
            "2wl2nyC+vJNFEaZVMOmCMQMKNnYqiY91kZdmjkkWcxV9xl15uXAfQdcEQYxqAWGG6c0siMUF1n6F6UDLHNtV00dTjxVXseHh+vFK" +
            "n+aeb47uq9xp1QLUHVgdIP8WibJgDLG8bCTQNoOGQXmh0cTsSv+zJ9YZzl2PiD3iU1EjXVy+e5HVu7O+ubW+tbNe291dYLyxtbve" +
            "eL6HF9Jucv1yxvR9Z5qQX/FVGJVe2dQYEPNXnwwU84bpoiFjbLwBzKcyzDYTu4ppB7tKPQ+AvuljefolubXgRYswKnbEgQjeq+HC" +
            "qNW3JdvSLS/eq9eFxsgnvE34hnJBbTNBfL7lbT9HiS2P7OyBnGWCMhSqu1jgbe4wEjW3BfLeTjBj00ERWU+3LpzlQZE3H2RgKcHC" +
            "MSw8F7HA55hkrp3dSjq+GrOconOMlyfExpoA+xS6Y8wwVKlJkCDkT9OmxM6QFkNE5JWER3cUOtHI9AMRohAOWBMCCb1JUE729vZS" +
            "ZXJy5T6vaKkr8UZ7lf3lqL6roPo3w+ej3ShnZ5ORCfMqs449fWkipwKT7hPXtG9yygoTU6068G0BAk7W5q5q+2wqaJmOeqzGpTNA" +
            "TvLvqpTr3ZEhVa65BNxOJXNCxAW7F9/HBQlGtFKutNDfR447BbXGtgcQ9LDKsJhD7CubOYV48X0Z0rUmRIUXCBhCwoGysPn1fq3a" +
            "SCiFCQwTrXsvVG6k5IoKu0j1zXolv1YzcIx7UQkMFYqGzNAKWDC0tCbY0mpsNhNTCyybKl5JRE+Ml3pD17GsAdY3OLP7E9POt4dM" +
            "scy9x+qKqVQHSJmwgPGXKpayM0KRKlMGMOe7aIYtZGyjkF0fzxLjFTRGrqjV0OUvMT+p6A91nXq+LWUIXUOph7vIoq7wPEZjSMA9" +
            "ohDW+X9SV35jM23pp9p14o4g/bxZWWaneK42Cpkpwi27uty2S6pzAe/u7OQnUasFR35ioYeKe7m8v4wpVWcmVB3U5c6OZcjyzTYc" +
            "NUcj/Eu9aWw/Nyp5p1q14CDhy0nbpvXVr/QNPaHM7mSstSXoWKXH1Sn6+XaGft7ItXfxLXE0Go62BcX+Fq8BbM4giLUG+k+VfMk9" +
            "nzuRUlTGMdFGDUobNHIJVmYqJbwOcYGxtYwCklzR6QrGZiWP3FtRDgfz5huLMH/L9DCHEHeo6EDI4weQafYF+ABSNaTFfk63goZU" +
            "+eK838ipmXHuNoajgajwSTQb6qVrNCsrUKXVwtAMbrC9YyAfmpZXxV9v1H6ZvL4ePlzVdvw81qlDJJaPx6s91zVbs2HBO5ZZqHrT" +
            "1FFvnisdTLv51hMFX6bcRPqTmByCnGkUpJjsFKmY7KToFtvby6Ipff8VpNtWyjLerRUAQ9Y2uZlviAxT+1FYOS/DQjPFGMemzm/2" +
            "1YaSUsduiaqdyOHNXNI8XSBHCZToN6FdJdaWypjeWZjNgQYe/cpOPhZf1Fp7nS76xq5wctPI5wIOCLWa6R2e19ATRxt53lojbjbo" +
            "Q8L2DqxqVZuZylgzl7fbx5KFhorcr+vUxYsCuVDU3dJE5uY2MxqwybA1er5ONnO0i0YK106j3tzerOedwYzFHi8DVrOORruI2DL1" +
            "4e6OoQCrvtMcNJ7nBesGesuAtDkwdpl5NYI7IxWm6lvNvU2YFyTIYrGXAQvVh9sUrMHe3qYSrM16Y2drkEslovFyKe7upOSL7oeb" +
            "W0qFYk/LNdHM9EoGUmR3mb18t5JDHRZMwa1thQN0J6kPx45/ybmLHg3C/XmKQTIyXHdLqL1C/3I/dF6dxnDheHkVeqe+r+S1BDMt" +
            "e6qedbLrO/PhhGstMosvMEN8OMMCejyxCE8ErtlIqIQmOIM5VuvsNBEvN0tXAuNvpsgwIViLxM88JxEqFWGQrDCfMJpnJ0GeaERP" +
            "XVbIx8UcuLa39fl2HWxuJ13JQpAPqZisIMqNHdlwaXElPLaEHJURoRWPLdmSxJaEgSJNWaBIU5hEBPHLsaTEmK/LjvX0BpIdwGa6" +
            "wvS6Do5313Uqq2SFaNMsB4ryvDar+xiysa2TtIvz0FMiq8WdUM7x4mmLQY9b9pJLRJcyykMT9fGmxPpckhoSb0DCfE6nxC5jeDnC" +
            "Mn0H+lBKTXCBZ57XpLAmIzK2NTC6tQKsMlM91VzPwTBSZ3BxHJHudNXQFsV51leC4/EEkcqZl77swsk/X2FUnVXXzBp9pXlruJYy" +
            "vRd6w8ndTGoka05C7hkSsbi1lYpF+W6tJbgyDZOko2WHLBdRjUlEr2quDpkJKKrm0pnFLMFmpmjfK3x5p9o3ggL6fJcooM3ndcnu" +
            "Gv/t74Mo+TDEnmhsNWy9I9t4NTEtYxFlvwikL8UC7COB+3lj9g3zsypiv4YlTOQmg4iOslDZtpH7tn9yHI/r/w4PAIYW9LyDp/GA" +
            "4acvvtvAhS+y6jIF9ikwDfqVjv2WfXsRp5jYnsZ34koincR6VMN8+uI//v2f/mlxCT68X8+hzOok0FSfvmj3Ds9Pu4fg1enJWftV" +
            "H5ydn2r2sVAxI9Mdo5f0y4u//Pc/gZPOyWW7D962j9sn7Z5mt6Fe/PTFG4SNXMBtAgPRS/yfoGtKehKpI+s5HvwmQzWzRqX2Drly" +
            "efCUVXgqrREZKYiRklfkyDrHs/PPHE9eiZLn4CmtRIMqPDNZ8cVf/uH/frfBgBIm/8vPp++MxxY6Me3UCR2hoWndYEJuAB9OZ/R3" +
            "2cT++OfFxNIIHfydsS6JIRJflS/JlwyOiZoNGkuTbveR+ARJE1Uzpnc/fRHcICTXBBULRtkFVS/CSb5l3vMf6ccXLzvHlyfgsNs+" +
            "edk9Vq1EyRp6pGkG+TlWmyJN4/E71/R+nZMM7yfSO4irTfWd6U+wZLzNP9NsKckCCmJ8z6yCcHSC4wv+TTIQXyaKyZHDnxeHnX67" +
            "ewy2OdXpN3V1CSx8S+jDGRjMbyARH3N/PlN1hb8HQGWSOjRRNGkcqvaK+ll8we/6ZvC/JmOc0IOvHGyRtggeY7Y0DQ+/gbz6dAkn" +
            "nsH75tSc/NrnHNyaLmbSpy5eBDN4/2uf9VVhM776K5hteD29mCkfYavFd+eev9y89XYAJu4zNJ/llGQ+lfZ0YFpc/8jULUMfg3Ds" +
            "L1EKGzXA9aMlld4ofExw5gdvqmj3olkDC7VmFfB60LzhWcvygzdTNcQW47/+EfTa3SMQpvRYHc4j6DXyA3kDPSmA//y/GIA0T9tq" +
            "wFHdkGd4yw9gEI6wCG+Rwvsv/43pdiSPzfnpjx0QqHx6tkuw8EqKObDUUsmRo9qRpEpyzV6YcFbTMqPkTh5iPaU4eWT+nUnE/Ig4" +
            "Zpj75ac5cu8v6OG14wo+nm8E20Xi8GEjTBeaT+4BIlqTsn8/Zl3kHiJunChH+UlQaHKPI2pEqSPFtIilhor1kDrW1UrjXGmNEd8w" +
            "lxoo3oVytNuoAZZ7oJj5phyDu5vzds7EhLLXWeCUy91x6M5T9o1lZej2WWKF8JZp3Qdesty9Bw0lnc9nBvRROLvrNe71dhERO+iM" +
            "Oa5xlbAE2XBgsYJDF47pd9ZXFAM1xx5a5vAmAusioSLNwiBGmNCPNTzq7Mx1MKEgqbtWERPGkP7p4CfcoX8drfMQgSWY9SOB4tLu" +
            "CSRn/J6XHJJwrCSqg9yidJ8bgbUnIX+KEPGMkNEInljKRxJMFpkfpTxxO3N/MzhuR1JtBcOZHt9cCExrFXFM1ifvh/viXrGUXSEY" +
            "QWJF0ll/kWwsu8Ne5x2I5DRT9XlI8osFEiO7V5osKtQ8ynJ8hUiOJzVKdMoTij6IdJTy4Mhxh+hHnj61JKR09KE7Rj5dRuI4LHvh" +
            "ojU4OMBzGTiOhWBEGAQ/30eqJgpb4AmTB1QpOsa0rZELRVih89akal85djCWHK2yH0NbvGuGBkHaxDuMB/9EsFCSjkBIHpUhIqVj" +
            "8kVNO9VYcTyWn5WlJS3MSX/8s4J1oql+w3y4sW5i+cSU8yfjN8pkrHpcJksZTiaLo0zmJ4Im+B4KPyMjvhtJJzKWTUQ6oSSHMH5t" +
            "lPdL2XI60ToAsCQw3UIURBP9lrJnjIVEQZM9O72QLoeYaUJENh6yIjlSFgW25JyaqySx/Tz4iWbnxQNIBk40eUJ6+vnn5HeWoLdm" +
            "eq8xJXwknznP4ov7qJH8XxUJpYvoG2+pkq7j9F8Om0w80duZdAYSpIpzBM9AeXYnsm60I5IOLbUfMp/sbmj0q6SjMrn4ndaQRVbp" +
            "tsSyeTqja65re6aBfjTRLcnSyVQ01aKSihyyMkXdJipy8ghCvJTigpAyt0eT7Zqj+7UvkvC5kS+PzWXJj0nc/5rSwcgQ6IxGGK5j" +
            "3JO0oiSUl0SwFDJo35nJx4zzb0VL8id1zCgdMrg+wSbpzF3e24kzchYTl8k1LlUDKfMmIIpxUiTleylbLivRlrIQksoZi1/CGuTu" +
            "frLo7jiJVMoO5NqhIhRqXRJfRHPN00ibdyTWCFRjDBN8Y+1Lqj0nBKqfoNxqML2l0VkCUOHHDKiI+SFhPXEVCi2SzCesoFh9BQ1M" +
            "OznfEBMsjSUDXrLaOW2V0/IVOE4dEzdKH1IUDZV97bVMPyW2mvQFTb6kNpEu6jShkb2oE6sx6TBILkIS6fQW2oaFpFyR4uCIeGjC" +
            "+LKyEsdUZVsMltfMJi3H5BbjARhBK3rLlZTS5I5XuKwu+f5e8Z0JGGkRXeakpJR0XIyJa8O0sUgLPRjZKiLza9BrTcgDv/1tegXM" +
            "hPbYn2gqZ5IC8nPXinf6of4R25Dk1uKV/DLNvbLB+0T9h1SNMAUFwwm0x8joZ2EiXq9YhMT71seLql1e9CihXECoguc+XiM+8oN8" +
            "+SxUS8LaTBDkZ11qQqdwLi3HYDkeeWND7vTg9+yC+wblx7FHAuvOlPonxOUraPIROeO7c+HaYShmaOe1O0np+7D0XlLKRU5yh07U" +
            "ZBJIvTNrGl6LUZe0uEJgfi2mVvw4kPVArqGyPUrWC7k+Wt7Xl1DQHiKLbJyZzMgazFz67yEawbnlr6UEr0vWJNG6s5bkk4Apv8aC" +
            "yMlk8pXP1wtWadkSkvg4VufPtKHvg6Hf6w6tZ9L/qngJ2cZjs5JaDdNZm4k8fTJnhzZ2U5SHXwLtC122Bg2jQ9oT7zzCVp3gmp86" +
            "GAnY5LMFt/xiPy7JNOYQmzrdEzkidB+KltV7n8+EvgNWk3etjRmqaVI0KFET//4FiwTPMz+jFuO/CG2WmRwdPw11jz86xqQCt48/" +
            "NlsnRQxfEpwbGaO7iNwoEwaOP/2YiEYaqQS8uEFJxIK03hNyfkJVDMme8EUVraXeFGQN5AIuYS4oxI3oZlv54ZuXl/3+aS84Uy7k" +
            "BZxF7BX5yQyIiEZlRg7gFh2khQzoGSzC4XpiC0g0gBGYEvsPPxSXNMv2xEedqOR2nNy+LE9EdEhXVnRY9qyYi0YSjYj8RF5EE6dT" +
            "isdJFMpX7E7BV2MrFk/3aFyViLLQ5Ky2RSPwvgZnQcsiAWoFc1YkeuSX5K541PDX4LJITLQml9mLFtB+HIzQMOWvhQ0SeZ0DFaR6" +
            "4ViQBz9/DYxEw701sTKdW9CMtgtCropDE723AaJ3OrHFG78WyKsGLWJ1feTDGZiiKbyBJoDW3AVYC4K1oHK8JzCB9j0E/nwMPXK3" +
            "dgDxh1ohxIDevT1cyO34rp2MyhHtCX/uRW/cJ/1Ef/nT/wYn5H436RgcIppEnr3SXqvVyqLEDOKzDXgvC4Mi37sXp2sVeTtq4Eja" +
            "hfp7xsELhbgWecfUQ9AdToBpz+b+B/K2/cFTZkRBHz39qLEDMLiwrHpkqIh9oQuTdCdmqJMZ/7aRvSWT7ZRAQwP55DOgA8h3VEpW" +
            "vd1UYyAMcN5hhN1WxlnzwdR8DNaqffqpyjqvMke+JgGfsEZSavgT17kFNroFHfo+tByYvjMdOBa4YDPjax74pgFvgEGSu89vsJAp" +
            "50YYA6xGZXJyocJbiEfzLIRmSbj26vVS6sw3lhPXwQ/e3dpvujR++KQNquCwQ59R5UJ2IyaihZa5patMqmGhx7OfSDiJYYb85zWh" +
            "mZRoKfq1ROooh1qWgZXMHL73XPWJgxDUDCboq3TO5dTOpOs8xup8Iir/gXACaM8ta19Z8aGUhTabhsNlYI3GMp44NrrPxhQHPxri" +
            "uxpCUltrhTBGf9h80yFSlqayYYQoHAf7paUIE6OstNbDuvx7Y7uOZYruPv1ZuAGkS+3oyk4lc2Jc4srqk33pHA0dVxKQF3l7PLmr" +
            "fU7EzScGwEani7wJdZetFS9P+5dvsKXUx9pw+y2Wq1WpClyMENXSNv/Hf43r2ZizJ9AzrRqIK6KBQaDWRCX67oJRgitjnVQRqyPR" +
            "c0j1vJKddm3axttQTvcjfJotGChGg5lySb6u1yrOBeXMRqkiL+8+oL0XZImdnHtC3n1hmb1BG1mZveTeJzT3ivT9QmvPyLVv6BAx" +
            "e/9I20NS9hElOeJiQsE4OswiSJpMRpFbTVm7TLDTBOtWvswDWPRMtfSdR1Oiy6W66GkJxTw2XKh8T4rz8JpAiFBiGklZ8REBD7ej" +
            "Mngmb/LdwH3xl3/9n//v//wpOUdrPg17aAFVF7EJ1qbI8+AYpdhsIWaQFCNa2+6//WMwTzlcSAHLQ8FHccwrF+TqEBxwNK2LYN/i" +
            "3wA0piRg/gVwfGcKfdMD9cfzrYXnFoX51qY0adYxnDkunk8wd7VzzeI1V/VlzE1i7FVt+BnADwb0IfnzBt0fPHUROb7V9kVxgKRr" +
            "kZdFvAlyJUvhUCA/O4JTQd/p83XdkP7iZKwKrV+VL1IDtJUdkk8YFmW39LRckppOMGiPx9AKl08B7q90D6XCDZr0TSZNxfSOpW5P" +
            "jW4L9W8qGIM6OemvJAXPV/JwFkjiVA9n/lORbaXEXs1byi8RO7feL+FuHNEHyZdxNuI9UcffyLaV6sSfWnjHzHYqZlgDFF7ZHQjx" +
            "53tWNbVO6/GdU+wyACYl+JBoIjeI8Wbcomo9CyGRK/U05x3W2iKpKxVWPrRM6CGvBT7wyqwuKJN/2Zfyx5Jk8jmhJd1d87SS6TBH" +
            "E1BmA01qh5VBmf8aTEIs6URnx74syqK1iptzkFZSY9JBVb1ZL2qDcvB7ML2wrLBpXOlN4Uob/CsO+lUE7KtCQQ6TPWrAHdbVAz5S" +
            "HZTDP4JpLEo/ZgQwfpRLBWb2HoAvD4LwGTkuWItKDuCMAhki86DRjj7wG0d0wh/j9/hkO2FkELIh4xHIblNRO06dGXIhFvmKfQK3" +
            "1j2HKjObg10fo2ZH0LdM51TtCVTRCBoqA1Qd2zdt8d6SyvHEJjp0pjOsCPWwMqyYq+24U8wrf0B9dJfijQ2Ay/b8kA3SgsMUV97G" +
            "h79rV/+2Xt37uDFWe3LL5bx+Ls5g8nlynqsRV/Sahg+cKGaaR32kao2vt5rnTDW8mItxaDsdX3JkwBhVDw7YWpfZKGpVKewAa4hD" +
            "a24gj0Oyn9nJQ5bKk6+hlKB0QTCSFbkc+HnwYyx73nXaqpfPkjcscppwijVFfwV3L4dpOTev0u+v7etn8Gtk+wjMbllN+U7Cs8zI" +
            "Cn/+GdTJhTE2uhTfmpuQ1k6n5Q3nEGIOW9edZCljRT7kOprVPe4MHH4SH7jEEUv9yyykuwW+G7zAdQj+ptBnjMmmFTMGPtK7fN9t" +
            "0MryDkmS0hNzssFU4rSOYxq7Rs80J+kZef81o8+FGpvd6ZVOh1eanZEko32SZDSzx4iut+h2v3hPfMARq7niN0Cbbm54wxx7Jo1O" +
            "Bd58BkkcKvWgTBGeNf6XBq/eY3GMiw04AdDgYawy9zdjvdhdJL61ilH9ATDFHAw8Qph7OLFYOLosGwi/iiLQa0xun+tFfqjdPNF8" +
            "yUv0H71bou5+xnIQL9F/1JTPGsXh6YeXHSaQAFnj3K0wxpVG/zdB1uFlBwnlhHokqr9wtlpOTrAjRnrDR4xMdy00wNTGa3luzWvi" +
            "Fe7gPrP0+m5CA3nCWVNQkZ8ELCV+D5lALLiTfQxRXVIeBeRAxg/QgjY5E4yfIhaNkVs0aM9m5wga95HbTgnAU3vl6xJ59Bwvfmci" +
            "Luq+/739e5vsAyXZuUdyi1icbSXakJ6irJJsHN35kg4Gxq41SdCadKhwGxNHwaUR/YRCERuZsRwbp5JsG1VBko05Y6a3DkRNsnnA" +
            "v+ntr6Rt7zLbhZIh2ThcCryH/XQeZBeKMRuNTFdyC45yVkmu/ufj1Iz1F5yEfArWXsi44nEItov8d3TpCIcVsuMh5wbZ8uSH/dOj" +
            "Tk/ij2eHVvI25Vmwu5clLUkW7Jb6wgFdS5JmfDlc3zimYtgLmlJSbaypF1RIKsm4bHVckxuMyw0bXV7ao5LhrvnaWm7Y2MLMN26w" +
            "KJcbOL6k8418t/Sod0uOGMqB5YYVxIjCE1AST7cUKQeIq6ay+l1cLhOuD097nfWMlaZ5jSgpldQRXKFIAi8752/bF93jxH4aswj5" +
            "VZrAHpSfrKbtlBHJAvkti/sjx5Q/ilNRRpJxq1UNKA0H5qfPBUEbvqxCE0HMbQONsLgwMs5X400zDlqj8gdvgjG5gP+OL1f84S76" +
            "hxZ/r4pRvEcViU7c3Qm059BaAtr95FqNVQp5u1ySJsgo0Pou7kp1zPImt5+TT0+UlNJGmTBdlDSCikr4WSJmCjE44iq7aHCACXRN" +
            "YNpmPssjNQRZxxiNX/pWmqHBCKtgIn4dg4e6Bn5McGSZN7IL1xYcm38V1hhl/YItMXbzpiGzjihywoH/8g//HF507PbKKZ01ZZ21" +
            "D7GxJRKI9EkntQhSPr3sS1r3HCyuZbNLlYEBS2nbigz+TRn8L9u9I7BBMUDBjv59fvrytJ+GkS1Zj/xVlkPanTxPQlqf27I+f+gc" +
            "tkGjjsHqY7KRji/ax4enoH30tnu+QDIpyAR6R8oTnfNLevX1rI+3wZfd9vs27a19dHmOvx62e21KUdb7X6v1SHny12Q53pBUHsUZ" +
            "jcHCuKaKwXIqfsriKlDDz6VhM1Gip10HKSS6dkE6Fj9y7RauXocy83TuFwRrQDvcY9HQXkDLcMAAr6NUWCkYn+ZTC05oi5e4gWpQ" +
            "cONYzjR7aHrnBa/kY2h/mvvgBimkqixCdin9NAlKcJP1uN374bKPBeJp//Sk3e9eSKqudIuVrg2sfJlT5Mz91eJudVJLBT8kxRTd" +
            "uEK1TkG1kDLfD/3ba5LJ78Agr5bdGuW84a1NVXRrqklChcGjmiNy7iJKAd/Zi7VSlNmIMsyWJ9L34tQ7aRY7aLFANtk1VOG5awnj" +
            "k1B9PP5aeno0taWBe6yxGwRn0IVTjziNkm+1UbiFsMxyMA15as00pJFBfYfvpwk2lB1vZ1OZB0uy0I8jJJ5EEruL0ei88/q8c/H2" +
            "+gz/0r1S2gb7pdRrZCtf8OdghC8v9jvnx52X3bd4rby9PL4s8oK/4hoQ8jyMX633zhZo1UiCLxHLsZE8vZEUSQIb5aU9kdL+/uPf" +
            "//xfwDkbVXgXMnFAqdgmM2/KbW7Xc+Y0dJHlQEN6Ly8hJmQhUHluOcIhFCT3QmAD25k6Lmj85Y//0thRX3007ZHzS1yFwUSaW1np" +
            "GUg8bEy+90nqnLXVsr/wkRXZeuO1aiRWm1+AAS9AfeUkL6zbXzzHC1YzclyjUVyHIqxBESIdojYyLR+5OuHLuA/NLB6moZFQgodN" +
            "6gUpkwewjNXzR3BS5M8hYRqVLNajuDbAiwNs8OlV/Q5X3Vn6steDZjArpbLnuL4OjeE6GOguCl0srsEaQV9Vt/qAVP+F0GKZabcK" +
            "6uqrBArC6d0AjoonzAT1ou78HmITNrFzAA9OZ9DEEBdwQVSuychdzbrqiOotQR3d5/G3ZxtNZ8giLjiZ4yBKSYnJAQaQnBim2v2q" +
            "8Fsy/AXykQUnwPTn4BMyIHGkGsg3b8Dc9uc3PG/qiDiiuYcwoR8ofYQKP2GGrzDNX5jlM6RTo/49yJPeKjpR+xAz/YisOdY9rglp" +
            "1J0IT5uWMnYf9dFtpSS7qpf8qOFZXGJtZKyP1E2RX+ci75dfJsxaLRNXz9zVA4aDEbeFDWShtFsdC9M439S5nz+IM+LXy+iT3erB" +
            "vjyk3SxTl4VTW1jc+aDV9TiHXue4sHkGXnd7+G9NX/TCH012E5pCJtVvKvhOX1KJp67KZWKq7zQE4ZzchdIbnabnpPXTBtcbNnYW" +
            "S48JM2HQ8nRre7vTPN455sE83+2biemG55x5aEnbh87+NJpm+8NDoI6QOx+bJCCyXzBWb3jPZ4+K1JcmvIcFQz4gfa4A9L58E0pU" +
            "VpzpltTuyaybO/rblWKbWu562L/9Y1zILX0jqBi/+6v2eRf02y87x4IHq1h/u8qxInPD/jQ3XWQkcmqUu4eiG7l93mmDV6eHHbGA" +
            "YfhdosFJu3cpLSBPi0g+n/YAJtKrzsVFcoSzyzPQPjm97PXlZa9kRdi4kX5/3UnM4WX7uN171VnoBx+T91ro1iG6TnIkdCnTDspq" +
            "tqb3OKWJT0hKaXqnk0CQcgCCfDChLya/QpYl8/K0XRfe10auM1Xd+sQj5EhOU/bJeNjuLOvemdWyfCOTyGcAp89eBwPLYIFhwm2N" +
            "TNfzq8OJaalRonYz6OVkYzOUzS6KtClUuLgX9sUQV0z14QQOSb00DRRC3KVWJleVZ+ZB14mJJRy6QzIsBCJNBwUkU5sOCjjOa3TU" +
            "01E6EkinjzFxuU+FI6KGPmNmzZovrawzYVqR+Cjr+vBmL85FQgENPiePfZ7LXdXFCzEymDKlVkXbs85PPcCHRK6apGiPiHeXyPZg" +
            "vlpoJAb5qjLOzS3gjGUkmn6ShmFc3EvrnEB/glf2XQ3OZlaKykxOUdSmNl80OdI8ZCTIyJEhg4jHtupobLFUmcjpZQmo4BFm9gxk" +
            "xnFKKBq15NhiS+mlSbTMAxUuUrKi8kIO+JB9JsLI90HrTCjATWblj6k1Pkb3NZKwo5yyGGp4rtM1paxX5xEyDbLJqvIIUc5JEWNY" +
            "Z89xqkFPNjIzJlG758Pf1at7tWpq0iTm3srn0cs4Bsw6+mPYKkDyPNHOD6M4XXw8ocFPqmdzb7Km6NE0WinwqhzwLoLXQ8dIc6Bn" +
            "8JtgFapJv15SPxpyfZsCPU0YxGhzhNDsEA1NrIqmQ6QDddxsXfblF8W0pvSq0teb18LsLnhixIH+9aYVOA0KnpRjX89cZ4g87yvN" +
            "K+L1KHhqvjObz65ZTqmvtsYijptHmd7wq8/u1WNM7tb4qjMLPWcFT2uE0FeaEfH5FTyZAbl0M/xaE0p4LHNOSqJ+LBXVwRUURRSd" +
            "4n0RahGoogqI9d5K980pKJJ+ls8glcw75bnpCAaS8XnFHQy0e2+O2ouQnCo465z3yYNiR+RqWueiexK+plvQGUEWby7uRsVISDzM" +
            "ftJEURl3/D25gwOKNlme0EWN9Lvy36uMi5bsEbiFARa/qktAFzMicrpGEv0m4/ltNIa++VnMZEu6q9EHDTxy1CJx+a8J7n4x5I/2" +
            "gGxD1b6SclwgoYMvs4fSjbyNdZVVJ0F4JW/nzHL8vdp0LGeEiS9JNsKn0jfLVDZlAnMZCYQyLUbJS2GZl0BBD/Z0L26HLClwFJ91" +
            "VBAnRlO+5sYLvgXVxn6GFCQV9wt+vejs9KIP3pyevjnugHedl+TUVnjCqG0brmMaYANcvSTSHgP7unvVCorDjwacQR9M8aZEAuct" +
            "kwTmkVgugLfssTe3x1gAGjBohneGGV7n5GCa/QJuSPQcDAv69zO89sEnzwlfHQeHyLvBOuhGH05nyJ069g26B3hsLAjJu+YkXJ+8" +
            "WW6Q3AVhP2S5Bx28ZoklMf8QQI35DQHrBhlz0q5WsLiPhPct1JN7cnlhsS69+Y2HvMXfNpzC5BawcM/LrzhdRCKzJFej2aCiuI5e" +
            "MgrL3pxc302tie/PztFPc+T5ogdkivyJIzFIy4SRxKjBuSu5L4y57Bpz2TWGW6jOz3xaMjWlzP2AVcIXZcXNZ+KhNlmw28Zd9fb2" +
            "tkqCPaoYDGQTx4uxP5yQTdg/uOy/ru7KghzL7eEQzXydEQhrrhMhtoHFsWmvf7vxbTn9niGJKEl2TOi6LsqmxSKQ4JqMXBZDGR2b" +
            "ULmVEt4Y9Kp0lCneH41KeDKFTJ83X9HZN0LiIiDNpb2x/D3b4Cciq6L4PSDYTGm19JXbTB9oiHYMhTPiqMVqWZlFv5aXvxyzOPfQ" +
            "uO7BgvTS3OfRmhod+ioXcVLDpMG+VDFeoxqHVrNW5quaj81Ir9vHxy/br44ANtgvj8hbxNh6ODntHXXe432y03vT7XXAcVt80f0X" +
            "5qsnlF4ZLwVEmY/kGyo7g09o6K/MfOonSHWsmFXkilK+0IOeJ8ww0n494Xt5P9rtW2lHNGnWk4rkT2R5j6SU1bmZkthbzkN9zMFy" +
            "ccyUKazHkAxjNb1XldPKs57N1RQxEblB0LGkLGBPMhDx59xkolTz1mwMmSQaspyR9CBQ+zSqlVtavelRms6bR2DqPidSfgPH0Fqd" +
            "C6Q3gXSpxnTm7Njd/FcdMkJqk0KOKMoXlC8K0om4rRyKHMZ02dvi95gx3vb7ZzRHr9Ca5P3XWLlEUKXJn5z8T3k/FCZZbKuxAMr8" +
            "kpyPXGLo1bK6jNAmo2OaD8abQst6kdWp7n5FGQgTOx7orKkU5W/TYm0ynkDPQsMGx4EyAC37PpVjU0DSrJD0tRU83eNDM+3ik4Df" +
            "7Nl/z4WndgO+HFZcCSwenl3upP4RzL2T+YC5HcAbxxlbiOysHrgYuia2O9OufzCkpNw4qJXlIQ6J44bi3OjcZwTOO+3D98U6UGJ3" +
            "V9Oz1kR8CuJWFilavH6VNKlP2heXR0ftHqlX1nUh5sg2eQLxtkVcYgTEDXSHhlGya+b7ByNoeSjdTei7YaBLcVS+aP/YwSR+dXpe" +
            "cJ4i5btIsUeQVCcj0ozOMbzRbkoJzXFxvfRLYXnyKJCtpaKq8uXc9s2pYhzy+nXNdm7XxGR6lcdMbfWm038U3lDli73h95SynrPn" +
            "W4ncoIgYEPLnN7Xy94ogZR/eaMaYqyx3lj0Qk5nY6imPxGufCEs8KXqZBbTi97IYn04nLSdk+pcVJ8ZrEiCkF+uuVWdGQt+FX1oL" +
            "EmCdtXudgg+h4w/ApbwbJS6/yNNQkTfm4m9BydqwZ1MSTcL3nWRtok84JVou3mxSNg3eSkm0vUtvdyVrE3lbSdkwfCQl0XqRC1vW" +
            "OLiMKHnSyUW2kTgwjGjRFiJ34NZxPdKbdCVQ9wavqHo4MbDENZR93lMsKlvxtiaFqaTWxaN3cFll/oRMKUUdf9k5vjwBh12alVum" +
            "6QYAev69hWpDx1K/+ZsFYPmbnc3RaA+W0wD6ZjQajra3Ewnvk8Qkz2QwsiffUmeLJ35Uz1dH/GPA+PGvd4kvyReqYndjaDxJ+GRC" +
            "7LllepkReqTgRxYkktA4IzOpYSp24HCyFj1hCphR5ENWQiw2ceOI0V/Gpgtgnx0sHCSRRvuyh6Wjk2A6cZy9YzZRRIkmq26NS0Ha" +
            "fj2QiZFxeDVGOl6L/ZGsRDB2xujJa3LqyquecirzugHR5ZWvFhXvlJWOAn7gNUP+SFYPpBWvGabZT1QsSQnEmpVUlCgl19kXrjZH" +
            "iJy859aKe+yL12BpoHP7VR/vGuCi0+8ct/He2+0dtsMclIVswkL2Tndut+e+06Z/xffiFVJo8tnE8wkcd3v99gU4PD2JnUktnzsz" +
            "zLnKgg6WCUsI09KwvDI5IoHCYYliLrHl5alnKuxsNUjNKnpicya+TU1suVevp2dSSEncmj/zLUvsJJBA//0Rwi4Zz22wEeThYPt6" +
            "sVMBkIQEXJ5KSMCfMdLJOmzpZLbInl8mLRt1LWJyyGP0S0XImiKpP0MRtCwiussyGzBaj206GRGPptd3oe2xdn+FuI280Pt4NtdZ" +
            "+00HvHrbeXVUrMElMHUU3dyaDAXhDPqTxFX2mu8cO7fIfQU9JNj1af5MtsZMTMvqje3cWsjA5Jz4UysZ3Rpl1QjYCZ75pUDHu0fV" +
            "XwyeD2qJEJfAvaZIYu54vjSXQHwmZOklwab5uGpDrFzdmrY3HWADZJoQcekIy0BaBuIoFBsumjmuv5HIQC5xZz9CkvvDdsGHAJEX" +
            "iRK+CuoHS2oexAu6JrGo77GeIaaSIQ5TvDu+xvrle1wsazbFpu5EM/g+6O+EtCGPKAWvj4VxHjNoXJDo9WTbZjJau1wvZ738bCQe" +
            "ls6CjaHn0cCSrjCKeuF9oKp41sUQnVULT/gROfisfX7RASenvc77R7j8ISQdK/LCR/J6vER25L+iQaPj1arnf17SUOL6K1/SUBLu" +
            "P69pPO41DZouxXXmtvDCoTDr4kXX69Pzk3b/MWSXNGOiQngF5JIwU/ycp0IVHayfIplgKyO7enlRfkR0scSA1Ety2A3eeBSuvpy5" +
            "JtahfeiFt10aNfDKgp4HBtiwgTZLwgrWgge9qtSNBTZAmEeUfakE7Zs18BrbdgM4vAFDElnJ0lBjXBnQgy4JELDgAFnFZyd8GxoE" +
            "1O/QYb7yBdaHZFok7cxCHi0gkSlfpouGvlZqPon8rsnskyrLuieN7ArBSxdFDCqFTGSFafuZ5KGGPNkG02YlprqqyLIdulg/EzXa" +
            "9FxwEW6RdazKb0gig51RPH92RX6APlwyryF5GSJXVsN1oEp3pXGMLs/2RTY7M37GEXIL+C6W/kpS49kz7cxgc9dVH4dppvNjqZjM" +
            "j5np/HLm+OKw0ceEGYcpglAlNZ+Bcqv8aJl25EQLCPcJE84kJtS+tMqnTALSWs+e5QLf89U6wOJ0LqnLqwn66aN2fkZ5rqG0ax7a" +
            "WZQUilf6KktFVkSwBhPNETi6wv3/Qrb/HtGWjkG/c9Uv+H1w+WJPV5dkKpDMrhOcaEobY+P33jOZZVEGaTYitw9FH97lbBa4o4oP" +
            "LOyQB6LPLgsmAgkGtGdzduK5QI9JvmWGFdJaNZ4lIUmSmMpBqxqmNyMRQ53PVKNKOKT49wQxaGvJ8wTySI3BfEBSEaek6XAlR6wP" +
            "Ar3lLxKuNo/hhCT3/5oTKZgpjzuds4IZMn4AMvWUooAg+gwrVSaJTBQvBTsW8bW8SH3dVS6naVO5q2HqSfwZj4jed+1un1irBUeL" +
            "B0/phbsvt7MWk/YZipZA/SLCpaSNWheRe5S6GiT1liFVWsZFYK86pyOZnUprofLQR+5nkm9G47GxzMtwGTfFxVA2jacJRbJlX4Zc" +
            "hBaR7rWuRDJLEkF3gQ2g+UPRq1VbC/AI6+jDwNMpFQxExlW8NAU+l64qZ2iN1+coB/P18aKkSyxnno2pv0qmIWJlraQNQ84LwOGu" +
            "zjcUkihlbo/HcxZJz045y9pdVUra01+VAXPc3lyBrHok1SJnQEqdYbMvrOmg8SHlxu26umxT9tapbLyHR1XL+u3zYuyEeBA8B1QI" +
            "x8NfHyr4n/8PEcv1/5xCAQA=";
 private WebView webView; private ProgressBar progressBar; private String injectedScript; private final ExecutorService executor=Executors.newSingleThreadExecutor();

 @SuppressLint({"SetJavaScriptEnabled","AddJavascriptInterface"})
 @Override protected void onCreate(Bundle savedInstanceState){
  super.onCreate(savedInstanceState);
  getWindow().setStatusBarColor(Color.rgb(6,29,39));
  getWindow().setNavigationBarColor(Color.rgb(6,29,39));
  injectedScript=decodeScript();

  LinearLayout root=new LinearLayout(this);
  root.setOrientation(LinearLayout.VERTICAL);
  root.setBackgroundColor(Color.rgb(6,29,39));

  LinearLayout toolbar=new LinearLayout(this);
  toolbar.setOrientation(LinearLayout.HORIZONTAL);
  toolbar.setPadding(dp(5),dp(4),dp(5),dp(4));
  toolbar.setBackgroundColor(Color.rgb(6,29,39));

  Button back=btn("‹"),history=btn("HISTORY"),transaction=btn("QRIS"),daily=btn("DAILY WD"),refresh=btn("↻");
  toolbar.addView(back,new LinearLayout.LayoutParams(dp(42),dp(42)));
  toolbar.addView(history,new LinearLayout.LayoutParams(0,dp(42),1f));
  toolbar.addView(transaction,new LinearLayout.LayoutParams(0,dp(42),1f));
  toolbar.addView(daily,new LinearLayout.LayoutParams(0,dp(42),1f));
  toolbar.addView(refresh,new LinearLayout.LayoutParams(dp(42),dp(42)));

  progressBar=new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
  progressBar.setMax(100);

  webView=new WebView(this);
  webView.setBackgroundColor(Color.WHITE);
  webView.addJavascriptInterface(new NativeBridge(),"AndroidBridge");

  WebSettings s=webView.getSettings();
  s.setJavaScriptEnabled(true);
  s.setDomStorageEnabled(true);
  s.setDatabaseEnabled(true);
  s.setUseWideViewPort(true);
  s.setLoadWithOverviewMode(true);
  s.setSupportZoom(true);
  s.setBuiltInZoomControls(true);
  s.setDisplayZoomControls(false);
  s.setJavaScriptCanOpenWindowsAutomatically(true);
  s.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
  s.setMediaPlaybackRequiresUserGesture(false);

  CookieManager.getInstance().setAcceptCookie(true);
  CookieManager.getInstance().setAcceptThirdPartyCookies(webView,true);

  webView.setWebViewClient(new WebViewClient(){
   @Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){ return false; }
   @Override public void onPageFinished(WebView v,String url){
    super.onPageFinished(v,url);
    if(isCitawin(url)) v.postDelayed(()->inject(),450);
   }
  });

  webView.setWebChromeClient(new WebChromeClient(){
   @Override public void onProgressChanged(WebView v,int p){
    progressBar.setProgress(p);
    progressBar.setVisibility(p>=100?View.GONE:View.VISIBLE);
   }
  });

  back.setOnClickListener(v->{ if(webView.canGoBack())webView.goBack(); });
  history.setOnClickListener(v->webView.loadUrl("https://citawin.idrbo2.com/historical-knowledge.html"));
  transaction.setOnClickListener(v->webView.loadUrl("https://citawin.idrbo2.com/new-transaction.html"));
  daily.setOnClickListener(v->webView.loadUrl("https://admin.citawinsmb.com/report/dailywd"));
  refresh.setOnClickListener(v->webView.reload());

  root.addView(toolbar,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(50)));
  root.addView(progressBar,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dp(3)));
  root.addView(webView,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1f));
  setContentView(root);
  webView.loadUrl(START_URL);
 }

 private Button btn(String t){
  Button b=new Button(this);
  b.setText(t);
  b.setTextSize(t.length()>2?10:20);
  b.setTextColor(Color.WHITE);
  b.setAllCaps(false);
  b.setBackgroundColor(Color.rgb(9,56,70));
  b.setPadding(dp(2),0,dp(2),0);
  return b;
 }

 private boolean isCitawin(String u){
  if(u==null)return false;
  u=u.toLowerCase();
  return u.startsWith("https://citawin.idrbo2.com/")||u.startsWith("https://admin.citawinsmb.com/");
 }

 private void inject(){
  if(injectedScript!=null&&!injectedScript.isEmpty())webView.evaluateJavascript(injectedScript,null);
 }

 private String decodeScript(){
  try{
   byte[] raw=Base64.decode(SCRIPT_B64,Base64.DEFAULT);
   try(GZIPInputStream gz=new GZIPInputStream(new ByteArrayInputStream(raw));ByteArrayOutputStream out=new ByteArrayOutputStream()){
    byte[] buf=new byte[8192];int n;
    while((n=gz.read(buf))!=-1)out.write(buf,0,n);
    return out.toString(StandardCharsets.UTF_8.name());
   }
  }catch(Exception e){
   Toast.makeText(this,"Script error: "+e.getMessage(),Toast.LENGTH_LONG).show();
   return "";
  }
 }

 private int dp(int v){ return Math.round(v*getResources().getDisplayMetrics().density); }

 @Override public void onBackPressed(){
  if(webView!=null&&webView.canGoBack())webView.goBack(); else super.onBackPressed();
 }

 @Override protected void onDestroy(){
  executor.shutdownNow();
  if(webView!=null)webView.destroy();
  super.onDestroy();
 }

 private class NativeBridge {
  private final android.content.SharedPreferences prefs=getSharedPreferences("citawin_shared_store",MODE_PRIVATE);

  @JavascriptInterface public String getItem(String k){
   return prefs.contains(k)?prefs.getString(k,"__CITAWIN_NULL__"):"__CITAWIN_NULL__";
  }
  @JavascriptInterface public void setItem(String k,String v){ prefs.edit().putString(k,v).apply(); }
  @JavascriptInterface public void removeItem(String k){ prefs.edit().remove(k).apply(); }

  @JavascriptInterface public void postForm(String url,String body,String id){
   executor.execute(()->{
    HttpResult r;
    try{ r=request(url,body,true,0); }
    catch(Exception e){ r=new HttpResult(false,0,"NETWORK ERROR: "+e.getMessage()); }
    HttpResult f=r;
    webView.post(()->webView.evaluateJavascript(
      "window.__citawinNativeResponse("+JSONObject.quote(id)+","+(f.ok?"true":"false")+","+f.status+","+JSONObject.quote(f.body==null?"":f.body)+");",null));
   });
  }
 }

 private HttpResult request(String target,String body,boolean post,int redirects)throws Exception{
  if(redirects>6)return new HttpResult(false,0,"Terlalu banyak redirect");
  HttpURLConnection c=(HttpURLConnection)new URL(target).openConnection();
  c.setInstanceFollowRedirects(false);
  c.setConnectTimeout(25000);
  c.setReadTimeout(30000);
  c.setRequestProperty("Accept","application/json,text/plain,*/*");
  c.setRequestProperty("Accept-Encoding","identity");
  c.setRequestProperty("User-Agent",webView.getSettings().getUserAgentString());

  if(post){
   c.setRequestMethod("POST");
   c.setDoOutput(true);
   c.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
   byte[] d=body.getBytes(StandardCharsets.UTF_8);
   c.setFixedLengthStreamingMode(d.length);
   try(OutputStream os=c.getOutputStream()){ os.write(d); }
  }else c.setRequestMethod("GET");

  int code=c.getResponseCode();
  if(code==301||code==302||code==303||code==307||code==308){
   String loc=c.getHeaderField("Location");
   c.disconnect();
   if(loc==null||loc.isEmpty())return new HttpResult(false,code,"Redirect tanpa Location");
   String next=new URL(new URL(target),loc).toString();
   boolean keep=code==307||code==308;
   return request(next,keep?body:"",keep,redirects+1);
  }

  InputStream stream=code>=200&&code<400?c.getInputStream():c.getErrorStream();
  StringBuilder sb=new StringBuilder();
  if(stream!=null)try(BufferedReader br=new BufferedReader(new InputStreamReader(stream,StandardCharsets.UTF_8))){
   String line;while((line=br.readLine())!=null)sb.append(line).append('
');
  }
  c.disconnect();
  return new HttpResult(code>=200&&code<300,code,sb.toString().trim());
 }

 private static class HttpResult {
  final boolean ok;final int status;final String body;
  HttpResult(boolean o,int s,String b){ok=o;status=s;body=b;}
 }
}
