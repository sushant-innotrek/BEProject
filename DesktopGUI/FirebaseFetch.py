from firebase import firebase


def fetch(otp):
    db = firebase.FirebaseApplication('https://srts-9b3fb.firebaseio.com', None)
    uid=None
    getUID = db.get('/OTP',None)

    if getUID:
        for key in getUID:
            if str(otp) == str(getUID[key]):
                uid = key
                break
    if uid==None:
        return None, None, None, None

    result = db.get('/Cart', uid)
    A,B,C = Parse(result)
    return A,B,C,uid

def RemoveOtp(uid):
    db = firebase.FirebaseApplication('https://srts-9b3fb.firebaseio.com', None)
    db.put('OTP', uid, "done")

def Parse(cart):
    Products = cart.values()
    
    Names=[]
    Price=[]
    Count=[]
    Total=[]

    Amount = 0 
    Weight = 0
    for product in Products:
        name  = product['name'].upper()
        price = product['price']
        count = product['count']
        total = price*count
        Weight += product['packagedWt']
        Names.append(name)
        Price.append(price)
        Count.append(count)
        Total.append(total)

        Amount += total
        
    FinalCart = {
        'Product' : Names,
        'Price' : Price,
        'Count' : Count,
        'Total' : Total,
    }    
    return FinalCart, Amount, Weight

def MoveCartToOrders(uid):
    db = firebase.FirebaseApplication('https://srts-9b3fb.firebaseio.com', None)
    result = db.get('/Cart', uid)
    result = db.post('/Orders/'+str(uid),result)
    db.delete('/Cart',uid)
    
