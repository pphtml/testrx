class FeatureMatrix {
    constructor(){
        const firefox = /.*Firefox.*/.test(navigator.userAgent);
        this.webGl = !firefox;
        this.googleFont = !firefox;
    }
}

export default FeatureMatrix