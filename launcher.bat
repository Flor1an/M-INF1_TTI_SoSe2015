START C:\progra~2\xap\gigaspaces-xap-premium-10.1.0-ga\bin\gs-agent.bat

TIMEOUT 20

START C:\progra~2\xap\gigaspaces-xap-premium-10.1.0-ga\bin\gs.bat deploy-space -cluster total_members=1,0 myTrafficGrid
TIMEOUT 10

START C:\progra~2\xap\gigaspaces-xap-premium-10.1.0-ga\bin\gs-ui.bat
TIMEOUT 15