import careIcon from '@/static/table/care_icon.svg'
import careIconActive from '@/static/table/care_onicon.svg'
import chatIcon from '@/static/table/chat_icon.svg'
import chatIconActive from '@/static/table/chat_onicon.svg'
import todoIcon from '@/static/table/todo_icon.svg'
import todoIconActive from '@/static/table/todo_onicon.svg'
import serviceIcon from '@/static/table/service_icon.svg'
import serviceIconActive from '@/static/table/service_onicon.svg'

export const dynamicTabBars = [
  { url: '/pagesA/chat/index',   text: 'home',    icon: chatIcon,    onicon: chatIconActive,    title: 'Chat'    },
  { url: '/pagesA/care/index',   text: 'calendar',icon: careIcon,    onicon: careIconActive,    title: 'Care'    },
  { url: '/pagesA/todo/index',   text: 'profile', icon: todoIcon,    onicon: todoIconActive,    title: 'Todo'    },
  { url: '/pagesA/service/index',text: 'service', icon: serviceIcon, onicon: serviceIconActive, title: 'Service' }
]
