<template>
  <web-view id="foo" v-if="isTrue" :src="previewUrl" class="w-[640px] h-[480px]"></web-view>

  <!-- 密码登录 -->
  <!--  w-full max-w-full h-screen-->
  <view v-if="login_type === 'passwd'" class="  w-full flex flex-col items-center p-4 box-border">
    <!--    <view class="max-w-[320px] w-full h-[140px]">-->
    <!--      <view class="text-[26px] tracking-[1px] text-text-primary-900">{{ $t('login.nihao') }}</view>-->
    <!--      <view class="text-text-primary-800">{{ $t('login.code') }} {{ left_title }}</view>-->
    <!--    </view>-->

    <view class="w-full max-w-[320px] text-[15px] flex justify-start border-b border-neutral-200 mb-5">
      <h2 class="text-primary-500 mb-2">{{ show_title }}</h2>
    </view>

    <uni-forms ref="formRef" class="max-w-[320px] w-full mx-auto" :modelValue="form" :rules="rules"
               label-width="auto" status-icon>
      <uni-forms-item name="phone">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput :placeholder="$t('login.enter_user_name')" v-model="form.phone"
                         class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"/>
        </view>
      </uni-forms-item>

      <uni-forms-item name="password">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput :placeholder="$t('login.enter_pass_wd')" type="password" v-model="form.password"
                         class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"/>
        </view>
      </uni-forms-item>

      <uni-forms-item>
        <button id="login_ref_button_kf" class="bg-primary-500 hover:bg-primary-600 text-white w-full h-[55px] mt-9 rounded-[30px]
                      shadow-md flex items-center justify-center transition-colors duration-300"
                @click="submitForm(formRef)">
          {{ $t('login.login_btn') }}
        </button>
      </uni-forms-item>
    </uni-forms>

    <h5 v-show="is_register" @click="to_page" class="text-primary-500 text-[13px] font-medium cursor-pointer mt-5">
      <span class="text-text-primary-900 font-medium">{{ $t('login.login_msg') }}</span>
      {{ $t('login.login_reg') }}
    </h5>
  </view>

  <!-- 短信登录 -->
  <view v-else-if="login_type === 'sms'" class=" flex flex-col items-center p-4 box-border">
    <!--    <view class="max-w-[320px] w-full h-[140px]">-->
    <!--      <view class="text-[26px] tracking-[1px] text-text-primary-900">{{ $t('login.nihao') }}</view>-->
    <!--      <view class="text-text-primary-800">{{ $t('login.code') }}</view>-->
    <!--    </view>-->

    <view class="w-full max-w-[320px] text-[15px] flex justify-start border-b border-neutral-200 mb-5">
      <h2 class="text-primary-500 mb-2">{{ show_title }}</h2>
    </view>

    <uni-forms class="max-w-[320px] w-full mx-auto" :modelValue="form_sms" :rules="rules_sms"
               ref="loginForm_sms" label-width="auto" status-icon>
      <uni-forms-item name="phone">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput v-model="form_sms.phone" :placeholder="$t('login.enter_phone')" type="number" maxlength="11"
                         class="placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"/>
        </view>
      </uni-forms-item>

      <uni-forms-item name="code">
        <view class="w-full flex items-center justify-center relative">
          <uni-easyinput class="w-[50px] placeholder-neutral-400 bg-neutral-50 rounded-[30px] border-none"
                         v-model="form_sms.code" :placeholder="$t('login.enter_verification_code')"/>
          <button class="absolute right-0 bg-transparent text-primary-600 text-[12px] border-none"
                  @click="sendcode" :disabled="countdown > 0">
            {{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
          </button>
        </view>
      </uni-forms-item>

      <button class="bg-primary-500 hover:bg-primary-600 text-white w-full h-[55px] mt-9 rounded-[30px]
                    shadow-md flex items-center justify-center transition-colors duration-300"
              @click="submitForm_sms">
        {{ $t('login.login_btn') }}
      </button>
    </uni-forms>

    <h5 v-show="is_register" @click="to_page" class="text-primary-500 text-[13px] font-medium cursor-pointer mt-5">
      <span class="text-text-primary-900 font-medium">{{ $t('login.login_msg') }}</span>
      {{ $t('login.login_reg') }}
    </h5>
  </view>

  <!-- H5登录 -->
  <view v-else-if="login_type === 'h5'" class=" flex flex-col items-center p-4 box-border">
    <button class="bg-success-500 hover:bg-success-600 text-white rounded-full px-8 py-3 text-lg"
            @click="login_click">{{ $t('login.wx_login') }}
    </button>
  </view>

  <!-- 微信登录 -->
  <view v-else class=" flex flex-col items-center p-4 box-border">
    <view class="w-full h-[188px] flex justify-center items-center text-[22px] font-medium text-text-primary-800">
      {{ props.title }}
    </view>

    <button class="bg-primary-500 hover:bg-primary-600 text-white rounded-[10px] w-[512rpx] h-[88rpx] text-[36rpx]
                  flex items-center justify-center"
            open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" v-if="radio1">
      {{ $t('login.wx_login') }}
    </button>

    <button class="bg-primary-500 hover:bg-primary-600 text-white rounded-[10px] w-[512rpx] h-[88rpx] text-[36rpx]
                  flex items-center justify-center"
            v-else @click="btnF1">
      {{ $t('login.wx_login') }}
    </button>

    <view class="radios flex justify-center text-[28rpx] mt-[48rpx] h-[300rpx]">
      <label class="radio flex justify-center items-center">
        <radio class="scale-[0.74] mr-[18rpx]" @click="isCheck" value="radio1" :checked="radio1"/>
        <span class="text-neutral-400">{{ $t('login.read_and_agree') }}
          <span class="text-primary-500 hover:text-primary-600 cursor-pointer"
                @click.stop="services">《{{ $t('login.service_agreement') }}》</span>
          <span>及</span>
          <span class="text-primary-500 hover:text-primary-600 cursor-pointer"
                @click.stop="policy">《{{ $t('login.privacy_agreement') }}》</span>
        </span>
      </label>
    </view>

    <view class="text-neutral-400 text-[13px] mt-[166px]">
      {{ end_content_show }}
    </view>
  </view>

  <view v-if="login_type === 'passwd' || login_type === 'sms' || login_type === 'h5'"
        class="w-full flex justify-center text-[12px] text-neutral-400 mb-1">
    {{ end_content_show }}
  </view>
</template>
<script>
export default {
  options: {
    styleIsolation: 'shared', // 解除样式隔离
  }
};
</script>
<script setup>
import {getCurrentInstance, ref} from "vue";
import service from "@/utils/request.js";
import {onShow} from "@dcloudio/uni-app";

const radio1 = ref(false)
const isCheck = () => {
  radio1.value = !radio1.value;
}


const isTrue = ref(false)
const {proxy} = getCurrentInstance();

const services = () => {
  proxy.$navigate("/pagesA/services/index", false)
}
const policy = () => {
  proxy.$navigate("/pagesA/policy/index", false)
}
const btnF1 = () => {
  uni.showToast({
    icon: "none",
    title: proxy.$tt('login.agreement_toast'),
    duration: 2000,
  });
  return;
}
const emit = defineEmits(["loginSuccess", "loginFail"]);

const props = defineProps({
  login_type: {type: String, default: 'passwd'},
  show_title: {type: String, default: "登陆"},
  relevanceTable: {type: String, required: true,},
  is_register: {type: String, required: false,},
  register_page: {type: String, required: false, default: ""},
  title: {type: String, required: false, default: "码上飞CodeFlying"},
  end_content: {type: String, required: false, default: "本应用由AI智能软件开发平台CodeFlying自动开发"}
});


const end_content_show = ref('')
let left_title = import.meta.env.VITE_APP_NAME;
let app_vip = import.meta.env.VITE_APP_VIP === 'true'
if (app_vip) {
  end_content_show.value = ''
} else {
  end_content_show.value = props.end_content
  left_title = "CodeFlying!"
}


const to_page = () => {
  proxy.$cf.navigate.to({
    url: props.register_page,
    type: 'page'
  })
}
const form = ref({
  phone: "",
  password: "",
});
// 验证码登陆的信息
const form_sms = ref({
  phone: '',
  code: null
});

const rules_sms = {
  phone: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('login.enter_phone')
      }
    ]
  },
  code: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('login.enter_verification_code')
      }
    ]
  }
};
const countdown = ref(0);
const isphoneValid = ref(false);
const openid = ref(null)

// 校验手机号
const validatephone = () => {
  if (form_sms.value.phone != null) {
    isphoneValid.value = /^1[3-9]\d{9}$/.test(form_sms.value.phone);
  }

};

const sendcode = async () => {
  validatephone()
  console.log(isphoneValid.value)
  if (!isphoneValid.value) {
    uni.showToast({
      title: proxy.$tt('login.phone_number_format_error_text'),
      icon: "none"
    })
    return;
  }

  try {
    let res = await proxy.$cf.login.getCodeByPhone(form_sms.value.phone)
    uni.showToast({
      title: proxy.$tt('login.verification_code_success_text'),
      icon: "success"
    })
    console.log(res);
    if (res.code === 0 && res.data) {
      form_sms.value.code = res.data;
    }
    countdown.value = 60;
    intervalId.value = setInterval(() => {
      if (countdown.value > 0) {
        countdown.value--;
      } else {
        clearInterval(intervalId.value);
      }
    }, 1000);
  } catch (e) {
    console.log(e)
    uni.showToast({
      title: proxy.$tt('login.verification_code_fail_text'),
      icon: "none"
    })
  }

};


const intervalId = ref(null);

const formRef = ref();

const rules = ref({
  phone: {
    rules: [
      {
        required: true,
        errorMessage: proxy.$tt('login.enter_user_name')
      },
      {
        minLength: 1,
        maxLength: 100,
        errorMessage: proxy.$tt('login.username_length_rule_text')
      }
    ]
  },
  password: {
    rules: [
      {required: true, errorMessage: proxy.$tt('login.enter_pass_wd')},
      {minLength: 3, maxLength: 18, errorMessage: proxy.$tt('login.password_length_rule_text')}
    ]
  }
});

const appId = import.meta.env.VITE_WX_APP_ID;
const callback = import.meta.env.VITE_WX_CALL_BACK;

/**
 ** 用户名+密码
 */
const submitForm = async (formEl) => {
  if (!formEl) return;
  await proxy.$refs.formRef.validate().then(res => {
    console.log(form.value)
    form.value.relevanceTable = props.relevanceTable
    form.value.openId = openid.value
    proxy.$cf.login.loginPasswd(form.value).then((res) => {
      login_success(res);
    }).catch(err => {
      console.error(err)
      login_error(err);
    });
  }).catch(err => {
    console.log("error submit!", err);
  });
};

/**
 * 手机号+验证码错误
 */
const submitForm_sms = () => {
  async function save_login_message() {
    var loin_sms_value = JSON.stringify({
      "phone": form_sms.value.phone,
      "code": form_sms.value.code,
    })
    var storage_key = await uni.setStorage({
      key: 'login_sms_key',      // 要存储的键
      data: loin_sms_value,    // 要存储的数据，可以是字符串、数字、对象等
    });
    return storage_key;
  }

  const loginForm = proxy.$refs.loginForm_sms
  loginForm.validate().then(() => {
    console.log(form_sms.value)

    form_sms.value.relevanceTable = props.relevanceTable
    proxy.$cf.login.loginBySms(form_sms.value).then(async (res) => {

      var storage_key = await save_login_message();
      console.log(storage_key, "storage_key")
      login_success(res);

    }).catch(async err => {
      var storage_key = await save_login_message();
      login_error(err);
    });
  }).catch(error => {
    uni.showToast({title: proxy.$tt('login.please_enter_correct_info'), icon: 'none'});

  });
};


function login_error(err) {
  emit("loginFail");
}

const login_success = async (res) => {
  uni.showToast({
    title: proxy.$tt('login.login_success_text'),
    icon: "success",
    duration: 2000
  });

  // uni.setStorageSync('h5_token', res.data)
  await proxy.$cf.globalVariable.write({
    variableName: "h5_token",
    value: res.data
  })
  console.log("=======================", res)
  proxy.$cf.login.getLoginUser().then((res) => {
    const item = res.data;
    // uni.setStorageSync("currentUser", JSON.stringify(item));
    proxy.$cf.globalVariable.write({
      variableName: "currentUser",
      value: item
    })
    emit("loginSuccess", item);
  }).catch(err => {
    console.log(err);
    emit("loginFail", err);
  });


}
const previewUrl = ref("")
const phone = ref("")


const getPhoneNumber = function (e) {
  console.log(e, "getPhoneNumber")
  if (e.detail.errMsg === "getPhoneNumber:fail user deny") {
    // 用户拒绝授权手机号
    uni.showToast({
      title: proxy.$tt('login.deny_authorization_text'),
      icon: "none",
      duration: 2000,
    });
  } else {
    // 用户同意授权手机号
    let token = uni.getStorageSync("token")
    uni.login({
      success: (res) => {
        console.log(res, "res============")

        service({
          url: "/login/phone",
          method: "get",
          data: {
            code: e.detail.code,
            relevanceTable: props.relevanceTable,
          }
        }).then((res) => {
          console.log("获取手机号之后的结果：", res)
          if (res.code === 0) {
            phone.value = res.data
            login_click();
          }

        })
      }
    })
  }
}

function login_click() {
  let url = `https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${encodeURIComponent(callback)}&response_type=code&scope=snsapi_base&state=codeflying#wechat_redirect`;
  // #ifdef MP-WEIXIN
  if (props.login_type === 'wechat') {
    console.log("进来了", import.meta.env.VITE_APP_SERVICE_API + '/login/wxApp')
    uni.login({
      "provider": "weixin",
      "onlyAuthorize": true, // 微信登录仅请求授权认证
      success: function (event) {
        const {code} = event
        //客户端成功获取授权临时票据（code）,向业务服务器发起登录请求。
        uni.request({
          url: import.meta.env.VITE_APP_SERVICE_API + '/login/wxApp', //仅为示例，并非真实接口地址。
          method: 'POST',
          data: {
            code: event.code,
            phone: phone.value,
            relevanceTable: props.relevanceTable,
          },
          success: (res) => {
            console.log(res, "loginSuccess")
            if (res.data.code === 0) {
              //获得token完成登录
              uni.setStorageSync('h5_token', res.data.data)
              // emit("loginSuccess")
              login_success(res)
            } else {
              login_error()
            }
          }
        });
      },
      fail: function (err) {
        login_error(err)
      }
    })
  }
  // #endif

  // #ifdef H5
  if (props.login_type === 'h5') {
    window.location.href = url;
  }
  // #endif
}

onShow(async () => {


})

function isWeixinBrowser() {
  // alert(window.navigator.userAgent.toLowerCase().toString())
  return typeof window !== 'undefined' &&
      window.navigator.userAgent.toLowerCase().toString().includes('micromessenger')
}

onLoad(async (options) => {

  //#ifdef MP-WEIXIN
  uni.login({
    success: async (res) => {
      console.log(res, "res============")
      let code = res.code
      let response = await proxy.$cf.login.getOpenIdByCode(code)
      if (response.success && response.data != null) {
        openid.value = response.data
      }
    }, fail: (error) => {
      console.error("登录失败", error)
    }
  })
//#endif
  //#ifdef H5
  let openidUrlParam = options.openid

  if ((openidUrlParam == null || openidUrlParam == undefined) && isWeixinBrowser()) {
    console.log("code1", openidUrlParam)
    //  微信内浏览器，刷新页面然后跳转回来获取code
    let res = await proxy.$cf.login.getMpUrl()
    console.log(res)
    if (res.success && res.data != null) {
      proxy.$cf.navigate.to({
        url: res.data,
        type: 'external'
      })
    }
  } else if (isWeixinBrowser() && openidUrlParam != null && openidUrlParam != undefined) {
    openid.value = openidUrlParam
  }
//#endif

  //  判断token 是否存在
  try {
    let res = await proxy.$cf.globalVariable.read({
      variableName: "h5_token",
    })
    if (res.success) {
      emit("loginSuccess")
    } else {
      login_setting();
    }
  } catch (e) {
    login_setting();
  }

})

function login_setting() {
  proxy.$cf.setting.login().then((res) => {
    let configList = res.data;
    if(!shouldShow('login.show.account', configList)){
      form.value.phone = '';
      form_sms.value.phone = '';
    }
    else{
      form.value.phone = '18852718858';
      form.value.password = '123456';
      form_sms.value.phone = '18852718858';
    }
  });

}

function shouldShow(nameKey, configList) {
  const item = configList.find(item => item.name === nameKey);
  return item && item.content === "true";
}


</script>

<style>
/* 仅保留必要的样式覆盖 */
::v-deep .uni-input-input {
  color: #999 !important;
}

::v-deep .uni-easyinput__content-input {
  padding-left: 20px !important;
}

::v-deep .uni-forms-item_error {
  width: 100%;
}

::v-deep.is-input-error-border .uni-easyinput__placeholder-class {
  color: #999;
}
</style>