from firebase import firebase
from random import randint 
def CreateRacks():
        db = firebase.FirebaseApplication('https://srts-9b3fb.firebaseio.com', None)
        products = db.get("/Products",None)
        Racks = [5,6,8,9,11,12,14,15,17,18,20,21,23,24,26,27,29,30,32,33,35,36,38,39,41,42,44,45,47,53]
        
        for barcode in products.keys():
                db.put("/RacksLayout/",str(barcode),str(Racks[randint(0,len(Racks)-1)]))
                
                
CreateRacks()
