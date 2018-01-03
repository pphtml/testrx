class FeatureMatrix {
    constructor(){
        const firefox = /.*Firefox.*/.test(navigator.userAgent);
        this.webGl = !firefox;
        this.googleFont = !firefox;
        //this.drawOwnSnakeFromServer = false;
        this.drawOwnSnakeFromServer = true;
    }

    getFontOptions() {
        return this.googleFont ?
            {fontFamily: "'Saira Extra Condensed'", fontSize: 24, fill: "white"} :
            {fontFamily: "Arial", fontSize: 20, fill: "white"}
    }
}

export default FeatureMatrix