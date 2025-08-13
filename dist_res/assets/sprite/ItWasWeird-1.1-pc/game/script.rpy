# У цьому файлі міститься скрипт гри.

# Оголошення символів, які використовуються в цій грі. Колірний аргумент розфарбовує
# ім'я персонажа.

define айко = Character(_("???"), image="айко")
define ічіко = Character(_("Ія"), image="ічіко")

image айко хмуриться = "Aiko_Casual_Frown.png"
image айко хмуриться закр = "Aiko_Casual_Closed_Frown.png"
image айко говорить = "Aiko_Casual_Open.png"
image айко говорить закр = "Aiko_Casual_Closed_Open.png"
image айко посмішка = "Aiko_Casual_Smile.png"
image айко посмішка закр = "Aiko_Casual_Closed_Smile.png"
image айко шок = "Aiko_Casual_Shocked_Frown.png"

# image ічіко хмуриться = "Ichiko_Casual_Frown.png"
# image ічіко хмуриться закр = "Ichiko_Casual_Closed_Frown.png"
# image ічіко норм = "Ichiko_Casual_Pout.png"
# image ічіко норм закр = "Ichiko_Casual_Closed_Pout.png"
# image ічіко говорить = "Ichiko_Casual_Shout.png"
# image ічіко говорить закр = "Ichiko_Casual_Closed_Shout.png"
# image ічіко посмішка = "Ichiko_Casual_Smile.png"
# image ічіко посмішка закр = "Ichiko_Casual_Closed_Smile.png"

image side айко хмуриться = "side_Aiko_Casual_Frown.png"
image side айко хмуриться закр = "side_Aiko_Casual_Closed_Frown.png"
image side айко говорить = "side_Aiko_Casual_Open.png"
image side айко говорить закр = "side_Aiko_Casual_Closed_Open.png"
image side айко посмішка = "side_Aiko_Casual_Smile.png"
image side айко посмішка закр = "side_Aiko_Casual_Closed_Smile.png"
image side айко шок = "side_Aiko_Casual_Shocked_Frown.png"

image side ічіко хмуриться = "side_Ichiko_Casual_Frown.png"
image side ічіко хмуриться закр = "side_Ichiko_Casual_Closed_Frown.png"
image side ічіко норм = "side_Ichiko_Casual_Pout.png"
image side ічіко норм закр = "side_Ichiko_Casual_Closed_Pout.png"
image side ічіко говорить = "side_Ichiko_Casual_Shout.png"
image side ічіко говорить закр = "side_Ichiko_Casual_Closed_Shout.png"
image side ічіко посмішка = "side_Ichiko_Casual_Smile.png"
image side ічіко посмішка закр = "side_Ichiko_Casual_Closed_Smile.png"

image side сора хмуриться = "side_Sora_Casual_Frown.png"
image side сора говорить = "side_Sora_Casual_Open.png"
image side сора посмішка = "side_Sora_Casual_Smile.png"

image кафе2 = "Restaurant_B.png"

image спальня = "Bedroom_Day.png"
image спальня ніч = "Bedroom_Night.png"

#Вино біле сухе з Європейського Союзу коштує менше 140 грн за знижкою

#Голова болить. 

#Грім схожий на вибухи

transform darken:
    matrixcolor Matrix([ 0.4, 0.0, 0.0, 0.0,
        0.0, 0.4, 0.0, 0.0,
        0.0, 0.0, 0.4, 0.0,
        0.0, 0.0, 0.0, 1.0, ])

transform flashback:
    matrixcolor Matrix([ 0.7, 0.15, 0.15, 0.0,
        0.0, 0.7, 0.0, 0.0,
        0.0, 0.0, 0.7, 0.0,
        0.0, 0.0, 0.0, 1.0, ])
    blur 10.0
# Гра починається тут.
screen chooselanguage():
    frame:
        padding (20, 20)
        xalign 0.5
        yalign 0.5
        has vbox
        textbutton "English" action [ Language("English"), Return()]
        textbutton "Українська" action [ Language(None), Return()]

label splashscreen:
    scene кафе2
    call screen chooselanguage
    scene black with dissolve

    show text _("{size=+10}{b}Попередження про вміст:{/b} \nзгадки про війну, \nповітряні тривоги \n(але без аудіо, не хвилюйтесь). {/size}") with dissolve
    with Pause(3)

    hide text with dissolve
    with Pause(1)

    return

label start:

    stop music fadeout 0.5

    $data_month = "ЛЮТИЙ"
    $data_day = 2

    scene black

    pause 0.5

    play music "audio/00 lolurio - Everyday Life.ogg"

    scene спальня with dissolve

    "Ранок. Не думала, що так рано прокинусь."

    "У мене ще багато часу до того, як іти на роботу."

    "Чи з'явились якісь новини?.."

    "О, Сашко якесь відео надіслав."

    "\"Не купляйте цю гру\"? Вроді відео коротке, гляну."

    scene black with dissolve

    centered "Через п'ятнадцять хвилин."

    scene спальня with dissolve

    "Що ж... Ще одне розчарування у людстві."

    "Хтось досі виправдовує покупку товарів, зроблених у ворожій терористичній країні?!"

    "Принаймні, ігор багато, і навіть якщо без них не можна обійтись, можна купити щось від своїх."

    "Мабуть, піду уже на роботу."

    play music "audio/Cute Bossa Nova.ogg"
    scene кафе2 with dissolve

    "Нарешті вдалося спокійно поспати... Хоча б сьогодні не будили серед ночі страшними звуками."

    "Надіюсь, це буде спокійний робочий день..."

    play sound "audio/door-open-with-bell-pixabay.ogg"

    pause 2.0

    stop sound

    show айко шок

    "Знервовані на вигляд."

    ічіко посмішка "Доброго дня. Замовляйте."

    show айко посмішка

    айко говорить "Доброго. Можна мені... маленький капучіно, будь ласка?"

    ічіко "Маленький капучіно... Щось ще?"

    айко говорить "Ні, дякую."

    ічіко "Тридцять п'ять."

    play sound "audio/cash_faster.ogg"

    pause 3.0

    stop sound

    ічіко "Дякую, що без решти."

    hide айко with dissolve

    "Тепер спокійніші."

    "Ноутбук... Може, боїться, що виженуть за роботу у кафе?"

    "Та тут стільки людей працює..."

    "Що ж, поки день іде нормально."

    play sound "audio/door-open-with-bell-pixabay.ogg"

    pause 2.0

    stop sound

    "Так, треба сконцентруватися на роботі."

    stop music fadeout 1.0

    scene black with dissolve

    centered "Після роботи."

    scene спальня ніч with dissolve

    "Треба лягати спати, інакше знову до першої години буду сидіти."

    scene black with dissolve

    "Чорт, тривога! Тільки почала засинати..."

    "Який шанс того, що сюди щось влетить?.."

    "Завеликий."

    stop sound

    centered "Через годину."

    play sound "audio/c-chord-83638.mp3"

    pause 2.0

    stop sound

    "Нарешті відбій. Посплю спокійно хоч кілька годин, може."

    centered "Ранок."

    play sound "audio/alarm-clock-short-6402.mp3"

    pause 9.0

    stop sound

    scene спальня with dissolve

    "Та щоб його..."

    $data_day = 3

    pause 2.0

    play music "audio/Cute Bossa Nova.ogg"

    scene кафе2 with dissolve

    "Як же дістали... За що все це, за існування?"

    "Доволі складно заснути, коли чуєш небезпеку. Навіть коли тобі щастить і нічого не трапляється з твоїм будинком."

    play sound "audio/door-open-with-bell-pixabay.ogg"

    pause 2.0

    stop sound

    show айко хмуриться with dissolve

    ічіко норм "...Доброго дня."

    айко хмуриться "Можна мені еспресо, будь ласка?"

    ічіко норм "Так, еспресо можна..."

    "Принаймні, перше замовлення просте, і його важче зробити неправильно, ніж щось фірмове." 
    
    "І не треба попереджати про арахіс, який є у тому одному рецепті..."

    pause 0.5

    айко хмуриться "Ех... Сильно стомлює сперечатися з тими, хто вважає, що шістдесят баксів ні на що не впливають."
    
    айко хмуриться "І що можна купляти ігри, проспонсовані компаніями загарбників."

    ічіко хмуриться "Повірити не можу, що хтось таке купляє. Друг недавно поділився відео, у якому про це розповідають... Не розумію я іноді людей."

    "Чому про це говорять саме зі мною? Я тут просто працюю."

    "Є, звичайно, люди, які розмовляють з незнайомцями про те, що тривожить... Але це заважає працювати."

    "Хоча еспресо вроді нормальне, і поки більше нікого немає."

    "...Чому я продовжую розмову?"

    ічіко говорить "Ось ваше еспресо. Двадцять три."

    play sound "audio/coin-payment-94727.ogg"
    
    pause 1.0

    stop sound

    hide айко with dissolve

    "Хух, більше не затримують."

    "...Чому таке скривлене обличчя?"

    "Може, еспресо заміцне?"

    ічіко говорить "Цукор на цьому столі!"

    show айко посмішка with dissolve

    айко "Дякую!"

    hide айко with dissolve

    "Як же стомилася за весь цей час..."

    play music "audio/00 lolurio - Everyday Life.ogg"

    scene спальня with dissolve

    "Узагалі не хочу готувати..."

    "Пофіг, просто з'їм сир з хлібом."

    "Мабуть, поганий сон мене довів до такого стану... Але вроді зазвичай не так тяжко."

    "Треба буде не забути лягти спати раніше, щоб скомпенсувати безсонні ночі."

    scene black with dissolve

    $data_day = 9
    pause 2.0

    play music "audio/Cute Bossa Nova.ogg"

    scene кафе2 with dissolve

    "Стає тепліше..."

    play sound "audio/door-open-with-bell-pixabay.ogg"

    pause 2.0

    stop sound

    show айко хмуриться with dissolve

    ічіко посмішка "Доброго дня."

    айко хмуриться "Можна мені маленький капучіно, будь ласка?"

    ічіко "Так, можна. Щось ще?"

    айко "Ні, дякую."

    ічіко "Тридцять п'ять."

    play sound "audio/cash_faster.ogg"

    pause 3.0

    stop sound

    ічіко "Решта п'ятнадцять."

    play sound "audio/counting-coins-into-a-bowl-102403.ogg"

    pause 1.0

    ічіко норм "Чайові?"

    айко "Надіюсь, не сильно потривожила недавно. Просто була стривожена тоді."

    "Про що вона говорить?"

    "Може, про той випадок?.."

    scene кафе2:
        flashback
    show айко хмуриться at center:
        flashback
    with pixellate

    айко хмуриться "Ех... Сильно стомлює сперечатися з тими, хто вважає, що шістдесят баксів ні на що не впливають."
    
    айко хмуриться "І що можна купляти ігри, проспонсовані компаніями загарбників."

    scene кафе2 
    show айко хмуриться
    with pixellate

    "Так, мабуть, це про той випадок. Я майже забула про нього."

    "Але їй дійсно не варто було мене відволікати..."

    ічіко норм "Пробачаю."

    ічіко "Але прошу більше не задавати питання, які не стосуються роботи. Це може заважати обслуговувати інших."

    айко "Добре."

    hide айко with dissolve

    "Що ж... Це було дивно."

    "Принаймні, все добре скінчилося. І, надіюсь, мене більше не будуть турбувати."

    stop music fadeout 1.0
    
    "Все добре скінчилося? Ні, це не так..."

    scene black with dissolve

    pause 1.0

    "Це просто незрозуміла розмова під час роботи скінчилася."

    "Безсонні ночі та страх нікуди не зникли."

    "І нероздумливість людей щодо того, що купляють, цьому зовсім не допомогає."

    "Принаймні, набагато легше пережити тривогу, коли є шанс відпочити від неї та жити звичайним життям."

    "На жаль, що багато у кого справи набагато гірші."

    pause 1.0

    return

