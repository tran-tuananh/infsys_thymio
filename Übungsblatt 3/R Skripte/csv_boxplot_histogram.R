library(ggplot2)
ecke <- read.csv("~/ecke.csv")
frontal <- read.csv("~/frontal.csv")
kante <- read.csv("~/kante.csv")
rechts <- read.csv("~/rechts.csv")
links <- read.csv("~/links.csv")
ggplot(ecke, aes(x=Sensor, y=Value)) + geom_boxplot() + ggtitle("Sensorwerte Ecke")
ggplot(frontal, aes(x=Sensor, y=Value)) + geom_boxplot() + ggtitle("Sensorwerte Frontal")
ggplot(kante, aes(x=Sensor, y=Value)) + geom_boxplot() + ggtitle("Sensorwerte Kante")
ggplot(rechts, aes(x=Sensor, y=Value)) + geom_boxplot() + ggtitle("Sensorwerte Rechts")
ggplot(links, aes(x=Sensor, y=Value)) + geom_boxplot() + ggtitle("Sensorwerte Links")
library(plyr)
mecke <- ddply(ecke, "Sensor", summarise, mValue = mean(Value))
ggplot(mecke, aes(x=factor(Sensor), y=mValue)) + geom_bar(stat="identity") + ggtitle("Sensorwerte Ecke")
mfrontal <- ddply(frontal, "Sensor", summarise, mValue = mean(Value))
ggplot(mfrontal, aes(x=factor(Sensor), y=mValue)) + geom_bar(stat="identity") + ggtitle("Sensorwerte Frontal")
mkante <- ddply(kante, "Sensor", summarise, mValue = mean(Value))
ggplot(mkante, aes(x=factor(Sensor), y=mValue)) + geom_bar(stat="identity") + ggtitle("Sensorwerte Kante")
mrechts <- ddply(rechts, "Sensor", summarise, mValue = mean(Value))
ggplot(mrechts, aes(x=factor(Sensor), y=mValue)) + geom_bar(stat="identity") + ggtitle("Sensorwerte Rechts")
mlinks <- ddply(links, "Sensor", summarise, mValue = mean(Value))
ggplot(mlinks, aes(x=factor(Sensor), y=mValue)) + geom_bar(stat="identity") + ggtitle("Sensorwerte Links")
