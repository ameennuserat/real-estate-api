package com.graduation.realestateconsulting.model.enums;

public enum ReportReason {
    /**
     * محتوى غير لائق أو مسيء يتعلق بقاصر.
     */
    UNDERAGE_ISSUE,

    /**
     * تنمر أو مضايقة أو أي نوع من أنواع الإساءة.
     */
    HARASSMENT_OR_BULLYING,

    /**
     * محتوى يشير إلى إيذاء النفس أو الانتحار.
     */
    SUICIDE_OR_SELF_HARM,

    /**
     * محتوى عنيف، يحض على الكراهية، أو مزعج بشكل عام.
     */
    VIOLENT_OR_HATEFUL_CONTENT,

    /**
     * محاولة بيع أو ترويج لسلع أو خدمات محظورة.
     */
    RESTRICTED_ITEMS,

    /**
     * محتوى مخصص للبالغين فقط.
     */
    ADULT_CONTENT,

    /**
     * احتيال، نصب، أو نشر معلومات كاذبة ومضللة.
     */
    SCAM_OR_FRAUD,

    /**
     * انتهاك لحقوق الملكية الفكرية، مثل استخدام محتوى مسروق.
     */
    INTELLECTUAL_PROPERTY,

    /**
     * سبب شخصي، لا يتعلق بانتهاك السياسات بشكل مباشر.
     */
    DISLIKE,

    /**
     * سبب آخر لم يتم ذكره، يتم توضيحه في حقل الوصف.
     */
    OTHER
}
